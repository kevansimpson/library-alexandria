package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Work;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.aravo.library.data.repository.EntityRowMappers.newWorkMapper;

@Component
public class WorkRepository implements CrudRepository<Work> {
    private final JdbcTemplate template;
    private final AuthorRepository authorRepository;
    private final AvailableFormatsRepository formatsRepository;
    private final ForwardRepository forwardRepository;
    private final VolumeRepository volumeRepository;

    @Autowired
    public WorkRepository(
            JdbcTemplate jdbc,
            AuthorRepository authors,
            AvailableFormatsRepository formats,
            ForwardRepository forwards,
            VolumeRepository volumes) {
        template = jdbc;
        authorRepository = authors;
        formatsRepository = formats;
        forwardRepository = forwards;
        volumeRepository = volumes;
    }

    @Override @Transactional
    public Work save(Work entity) {
        Work work = CrudRepository.super.save(entity);
        authorRepository.syncAuthors(work);
        formatsRepository.syncAvailableFormats(work);
        forwardRepository.syncForward(work);
        volumeRepository.syncVolumeInfo(work);
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
                "UPDATE WORKS SET TITLE = ?, PUBLISHED = ?, RARE = ? WHERE ID = ?",
                work.getTitle(), work.getPublished(), work.isRare(), work.getId());
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
            work.setForward(forwardRepository.findForwardByWorkId(work.getId()));
            work.setVolumeInfo(volumeRepository.findVolumeInfoByWorkId(work.getId()));
        }
        return work;
    }
}
