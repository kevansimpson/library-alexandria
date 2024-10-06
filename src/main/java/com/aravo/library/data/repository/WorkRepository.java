package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Work;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

import static com.aravo.library.data.repository.EntityRowMappers.newWorkMapper;

@Component
public class WorkRepository implements CrudRepository<Work> {
    private final JdbcTemplate template;
    private final AuthorRepository authorRepository;
    private final AvailableFormatRepository formatsRepository;
    private final CitationRepository citationRepository;
    private final ForwardRepository forwardRepository;
    private final VolumeRepository volumeRepository;

    @Autowired
    public WorkRepository(
            JdbcTemplate jdbc,
            AuthorRepository authors,
            AvailableFormatRepository formats,
            CitationRepository citations,
            ForwardRepository forwards,
            VolumeRepository volumes) {
        template = jdbc;
        authorRepository = authors;
        citationRepository = citations;
        formatsRepository = formats;
        forwardRepository = forwards;
        volumeRepository = volumes;
    }

    @Override @Transactional
    public Work save(Work entity) {
        Work work = CrudRepository.super.save(entity);
        authorRepository.syncAuthors(work);
        citationRepository.syncCitations(work);
        formatsRepository.syncAvailableFormats(work);
        forwardRepository.syncForward(work);
        volumeRepository.syncVolumeInfo(work);
        return work;
    }

    @Override
    public Work create(Work work) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO WORKS (TITLE, PUBLISHED, RARE) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, work.getTitle());
            stmt.setDate(2, Date.valueOf(work.getPublished()));
            stmt.setBoolean(3, work.isRare());
            return stmt;
        }, keyHolder);
        //noinspection DataFlowIssue
        return findById(keyHolder.getKeyAs(Long.class));
    }

    @Override
    public List<Work> findAll() {
        return template.query(
                "SELECT * FROM WORKS ORDER BY TITLE", newWorkMapper())
                .stream().map(this::load).collect(Collectors.toList());
    }

    @Override
    public Work findById(long id) {
        try {
            return load(template.queryForObject("SELECT * FROM WORKS WHERE ID = ?", newWorkMapper(), id));
        }
        catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Work update(Work work) {
        int update = template.update(
                "UPDATE WORKS SET TITLE = ?, PUBLISHED = ?, RARE = ? WHERE ID = ?",
                work.getTitle(), work.getPublished(), work.isRare(), work.getId());
        return (update == 0) ? null : work;
    }

    @Override @Transactional
    public void delete(long id) {
        authorRepository.unlinkAuthorsByWorkId(id);
        citationRepository.deleteCitationsByWorkId(id);
        formatsRepository.deleteFormatsByWorkId(id);
        forwardRepository.deleteForwardByWorkId(id);
        volumeRepository.deleteVolumeInfoByWorkId(id);
        template.update("DELETE FROM WORKS WHERE ID = ?", id);
    }

    protected Work load(Work work) {
        if (work != null) {
            authorRepository.findAuthorsByWorkId(work.getId()).forEach(work::addAuthor);
            citationRepository.findCitationsByWorkId(work.getId()).forEach(work::addCitation);
            formatsRepository.findFormatsByWorkId(work.getId()).forEach(work::addFormat);
            work.setForward(forwardRepository.findForwardByWorkId(work.getId()));
            work.setVolumeInfo(volumeRepository.findVolumeInfoByWorkId(work.getId()));
        }
        return work;
    }
}
