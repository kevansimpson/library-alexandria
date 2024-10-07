package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Forward;
import com.aravo.library.data.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

import static com.aravo.library.data.repository.EntityRowMappers.newForwardMapper;

@Repository
public class ForwardRepository implements CrudRepository<Forward>{

    private final JdbcTemplate template;
    private final OrphanRemover orphanRemover;

    @Autowired
    public ForwardRepository(JdbcTemplate jdbc, OrphanRemover remover) {
        template = jdbc;
        orphanRemover = remover;
    }

    public Forward findForwardByWorkId(long workId) {
        try {
            return template.queryForObject("SELECT * FROM FORWARDS WHERE WORK_ID = ?",
                    newForwardMapper(), workId);
        }
        catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public void deleteForwardByWorkId(long workId) {
        template.update("DELETE FROM FORWARDS WHERE WORK_ID = ?", workId);
    }

    @Override
    public Forward create(Forward forward) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO FORWARDS (WORK_ID, AUTHOR, FORWARD) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, forward.getWorkId());
            stmt.setString(2, forward.getAuthor());
            stmt.setString(3, forward.getForwardText());
            return stmt;
        }, keyHolder);
        //noinspection DataFlowIssue
        return findById(keyHolder.getKeyAs(Long.class));
    }

    @Override
    public List<Forward> findAll() {
        return template.query(
                "SELECT * FROM FORWARDS ORDER BY WORK_ID, AUTHOR", newForwardMapper());
    }

    @Override
    public Forward findById(long id) {
        try {
            return template.queryForObject(
                    "SELECT * FROM FORWARDS WHERE ID = ?", newForwardMapper(), id);
        }
        catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Forward update(Forward forward) {
        int update = template.update(
                "UPDATE FORWARDS SET WORK_ID = ?, AUTHOR = ?, FORWARD = ? WHERE ID = ?",
                forward.getWorkId(), forward.getAuthor(), forward.getForwardText(), forward.getId());
        return (update == 0) ? null : forward;
    }

    @Override
    public boolean delete(long id) {
        return template.update("DELETE FROM FORWARDS WHERE ID = ?", id) > 0;
    }

    protected void syncForward(Work work) {
        Forward forward = work.getForward();
        if (forward != null) {
            forward.setWorkId(work.getId());
            work.setForward(save(forward));
            orphanRemover.removeOrphans(work, Set.of(work.getForward()), "FORWARDS");
        }
        else
            orphanRemover.removeAllOrphans(work, "FORWARDS");
    }
}
