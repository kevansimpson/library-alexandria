package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Citation;
import com.aravo.library.data.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aravo.library.data.repository.EntityRowMappers.newCitationMapper;

@Repository
public class CitationRepository implements CrudRepository<Citation> {

    private final JdbcTemplate template;
    private final OrphanRemover orphanRemover;

    @Autowired
    public CitationRepository(JdbcTemplate jdbc, OrphanRemover remover) {
        template = jdbc;
        orphanRemover = remover;
    }

    public List<Citation> findCitationsByWorkId(long workId) {
        return template.query("SELECT * FROM CITATIONS WHERE WORK_ID = ?", newCitationMapper(), workId);
    }

    public void deleteCitationsByWorkId(long workId) {
        template.update("DELETE FROM CITATIONS WHERE WORK_ID = ?", workId);
    }

    @Override
    public Citation create(Citation citation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO CITATIONS (WORK_ID, PAGE_NUMBER, CITED_WORK, CITATION_AUTHOR, CITED_WHEN) VALUES (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, citation.getWorkId());
            stmt.setInt(2, citation.getPageNumber());
            stmt.setString(3, citation.getCitedWork());
            stmt.setString(4, citation.getCitationAuthor());
            stmt.setDate(5, Date.valueOf(citation.getCitedOn()));
            return stmt;
        }, keyHolder);
        //noinspection DataFlowIssue
        return findById(keyHolder.getKeyAs(Long.class));
    }

    @Override
    public List<Citation> findAll() {
        return template.query("SELECT * FROM CITATIONS", newCitationMapper());
    }

    @Override
    public Citation findById(long id) {
        try {
            return template.queryForObject("SELECT * FROM CITATIONS WHERE ID = ?", newCitationMapper(), id);
        }
        catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Citation update(Citation citation) {
        int update = template.update(
                "UPDATE CITATIONS SET WORK_ID = ?, PAGE_NUMBER = ?, CITED_WORK = ?, CITATION_AUTHOR = ?, CITED_WHEN = ? WHERE ID = ?",
                citation.getWorkId(), citation.getPageNumber(), citation.getCitedWork(),
                citation.getCitationAuthor(), citation.getCitedOn(), citation.getId());
        return (update == 0) ? null : citation;

    }

    @Override
    public boolean delete(long id) {
        return template.update("DELETE FROM CITATIONS WHERE ID = ?", id) > 0;
    }

    protected void syncCitations(Work work) {
        Set<Citation> citations = work.getCitations().stream()
                .map(c -> {
                    c.setWorkId(work.getId());
                    return save(c);
                })
                .collect(Collectors.toSet());
        orphanRemover.removeOrphans(work, citations, "CITATIONS");
        work.setCitations(citations);
    }
}
