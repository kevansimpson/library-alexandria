package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Author;
import com.aravo.library.data.entity.Work;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class WorkRepository implements CrudRepository<Work> {
    private final JdbcTemplate template;
    private final AuthorRepository authorRepository;

    @Autowired
    public WorkRepository(JdbcTemplate jdbc, AuthorRepository authors) {
        template = jdbc;
        authorRepository = authors;
    }

    @Override @Transactional
    public Work save(Work entity) {
        Work work = CrudRepository.super.save(entity);
        syncAuthors(work);
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
                "SELECT * FROM WORKS ORDER BY TITLE", new WorkMapper())
                .stream().map(this::load).collect(Collectors.toList());
    }

    @Override
    public Work findById(long id) {
        return load(template.queryForObject(
                "SELECT * FROM WORKS WHERE ID = ?", new WorkMapper(), id));
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
        }
        return work;
    }
    protected Work syncAuthors(Work work) {
        Set<Author> authors = work.getAuthors().stream()
                .map(authorRepository::save).collect(Collectors.toSet());
        authors.forEach(a -> linkAuthorWork(work.getId(), a.getId()));
        work.setAuthors(authors);
        return work;
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

    private static class WorkMapper implements RowMapper<Work> {
        @Override
        public Work mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Work(
                    rs.getInt("ID"),
                    rs.getString("TITLE"),
                    rs.getDate("PUBLISHED"),
                    rs.getBoolean("RARE"));
        }
    }

}
