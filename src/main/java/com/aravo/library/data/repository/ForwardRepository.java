package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Forward;
import com.aravo.library.data.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aravo.library.data.repository.EntityRowMappers.newForwardMapper;

@Component
public class ForwardRepository implements CrudRepository<Forward>{

    private final JdbcTemplate template;

    @Autowired
    public ForwardRepository(JdbcTemplate jdbc) {
        template = jdbc;
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

    @Override
    public Forward create(Forward forward) {
        int update = template.update(
                "INSERT INTO FORWARDS (WORK_ID, AUTHOR, FORWARD) VALUES (?, ?, ?)",
                forward.getWorkId(), forward.getAuthor(), forward.getForwardText());
        return (update == 0) ? null
                : template.queryForObject("SELECT * FROM FORWARDS WHERE WORK_ID = ? AND AUTHOR = ?",
                newForwardMapper(), forward.getWorkId(), forward.getAuthor());
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
                "UPDATE FORWARDS SET WORK_ID = ?, AUTHOR = ?, FORWARD WHERE ID = ?",
                forward.getWorkId(), forward.getAuthor(), forward.getForwardText(), forward.getId());
        return (update == 0) ? null : forward;
    }

    @Override
    public void delete(Forward forward) {
        template.update("DELETE FROM FORWARDS WHERE ID = ?", forward.getId());
    }

    protected void syncForward(Work work) {
        Forward forward = work.getForward();
        if (forward != null) {
            forward.setWorkId(work.getId());
            work.setForward(save(forward));
        }
        else
            work.setForward(findForwardByWorkId(work.getId()));
    }
}
