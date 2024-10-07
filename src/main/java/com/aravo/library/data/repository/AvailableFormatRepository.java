package com.aravo.library.data.repository;

import com.aravo.library.data.entity.AvailableFormat;
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
import java.util.stream.Collectors;

import static com.aravo.library.data.repository.EntityRowMappers.newAvailableFormatMapper;

@Repository
public class AvailableFormatRepository implements CrudRepository<AvailableFormat> {

    private final JdbcTemplate template;
    private final OrphanRemover orphanRemover;

    @Autowired
    public AvailableFormatRepository(JdbcTemplate jdbc, OrphanRemover remover) {
        template = jdbc;
        orphanRemover = remover;
    }

    public List<AvailableFormat> findFormatsByWorkId(long workId) {
        return template.query("SELECT * FROM FORMATS WHERE WORK_ID = ?", newAvailableFormatMapper(), workId);
    }

    public void deleteFormatsByWorkId(long workId) {
        template.update("DELETE FROM FORMATS WHERE WORK_ID = ?", workId);
    }

    @Override
    public AvailableFormat create(AvailableFormat format) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO FORMATS (WORK_ID, FORMAT, SHIPPING_COST) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, format.getWorkId());
            stmt.setInt(2, format.getWorkFormat().ordinal());
            stmt.setBigDecimal(3, format.getShippingCost());
            return stmt;
        }, keyHolder);
        //noinspection DataFlowIssue
        return findById(keyHolder.getKeyAs(Long.class));
    }

    @Override
    public List<AvailableFormat> findAll() {
        return template.query(
                "SELECT * FROM FORMATS", newAvailableFormatMapper());
    }

    @Override
    public AvailableFormat findById(long id) {
        try {
            return template.queryForObject(
                    "SELECT * FROM FORMATS WHERE ID = ?", newAvailableFormatMapper(), id);
        }
        catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public AvailableFormat update(AvailableFormat format) {
        int update = template.update(
                "UPDATE FORMATS SET WORK_ID = ?, FORMAT = ?, SHIPPING_COST = ? WHERE ID = ?",
                format.getWorkId(), format.getWorkFormat().ordinal(), format.getShippingCost(), format.getId());
        return (update == 0) ? null : format;

    }

    @Override
    public boolean delete(long id) {
        return template.update("DELETE FROM FORMATS WHERE ID = ?", id) > 0;
    }


    protected void syncAvailableFormats(Work work) {
        Set<AvailableFormat> formats = work.getFormats().stream()
                .map(f -> {
                    f.setWorkId(work.getId());
                    return save(f);
                })
                .collect(Collectors.toSet());
        orphanRemover.removeOrphans(work, formats, "FORMATS");
        work.setFormats(formats);
    }
}
