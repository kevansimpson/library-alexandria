package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Author;
import com.aravo.library.data.entity.AvailableFormats;
import com.aravo.library.data.entity.Work;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aravo.library.data.repository.EntityRowMappers.newWorkMapper;

@Component
public class WorkRepository implements CrudRepository<Work> {
    private final JdbcTemplate template;
    private final AuthorRepository authorRepository;
    private final AvailableFormatsRepository formatsRepository;

    @Autowired
    public WorkRepository(JdbcTemplate jdbc, AuthorRepository authors, AvailableFormatsRepository formats) {
        template = jdbc;
        authorRepository = authors;
        formatsRepository = formats;
    }

    @Override @Transactional
    public Work save(Work entity) {
        Work work = CrudRepository.super.save(entity);
        syncAuthors(work);
        syncAvailableFormats(work);
        return work;
    }

    @Override
    public Work create(Work work) {
        int update = template.update(
                "INSERT INTO WORKS (TITLE, PUBLISHED, RARE) VALUES (?, ?, ?)",
                work.getTitle(), work.getPublished(), work.isRare());
        Integer created = (update == 0) ? null
                : template.queryForObject("SELECT ID FROM WORKS WHERE TITLE = ? AND PUBLISHED = ?",
                Integer.class, work.getTitle(), work.getPublished());
        if (created != null)
            work.setId(created);
        return work;
    }

    @Override
    public List<Work> findAll() {
        return template.query(
                "SELECT * FROM WORKS ORDER BY TITLE", newWorkMapper())
                .stream().map(this::load).collect(Collectors.toList());
    }

    @Override
    public Work findById(long id) {
        return load(template.queryForObject(
                "SELECT * FROM WORKS WHERE ID = ?", newWorkMapper(), id));
    }

    @Override
    public Work update(Work work) {
        int update = template.update(
                "UPDATE WORKS SET TITLE = ?, PUBLISHED = ?, RARE = ?",
                work.getTitle(), work.getPublished(), work.isRare());
        return (update == 0) ? null : work;
    }

    @Override
    public void delete(Work work) {
        template.update("DELETE FROM WORKS WHERE ID = ?", work.getId());
    }

    protected Work load(Work work) {
        if (work != null) {
            authorRepository.findAuthorsByWorkId(work.getId()).forEach(work::addAuthor);
            formatsRepository.findFormatsByWorkId(work.getId()).forEach(work::addFormat);
        }
        return work;
    }

    protected void syncAuthors(Work work) {
        Set<Author> authors = work.getAuthors().stream()
                .map(authorRepository::save).collect(Collectors.toSet());
        authors.forEach(a -> linkAuthorWork(work.getId(), a.getId()));
        work.setAuthors(authors);
    }

    protected void linkAuthorWork(long workId, long authorId) {
        Integer exists = template.queryForObject(
                "SELECT count(*) FROM AUTHOR_WORK_XREF WHERE work_id = ? AND author_id = ?",
                Integer.class, workId, authorId);
        if (exists == null || exists == 0)
            template.update(
                    "INSERT INTO AUTHOR_WORK_XREF (work_id, author_id) VALUES (?, ?)",
                    workId, authorId);
    }

    protected void syncAvailableFormats(Work work) {
        Set<AvailableFormats> formats = work.getFormats().stream()
                .map(f -> {
                    f.setWorkId(work.getId());
                    return formatsRepository.save(f);
                })
                .collect(Collectors.toSet());
        formats.forEach(f -> linkFormatWork(work.getId(), f));
        work.setFormats(formats);
    }

    protected void linkFormatWork(long workId, AvailableFormats format) {
        Integer exists = template.queryForObject(
                "SELECT count(*) FROM FORMATS WHERE work_id = ? AND format = ?",
                Integer.class, workId, format.getWorkFormat().ordinal());
        if (exists == null || exists == 0)
            template.update(
                    "INSERT INTO FORMATS (work_id, format, shipping_cost) VALUES (?, ?, ?)",
                    workId, format.getWorkFormat().ordinal(), format.getShippingCost());
    }
}
