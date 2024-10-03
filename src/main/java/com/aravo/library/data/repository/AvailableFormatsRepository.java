package com.aravo.library.data.repository;

import com.aravo.library.data.entity.AvailableFormats;
import com.aravo.library.data.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aravo.library.data.repository.EntityRowMappers.newAvailableFormatsMapper;

@Component
public class AvailableFormatsRepository implements CrudRepository<AvailableFormats> {

    private final JdbcTemplate template;

    @Autowired
    public AvailableFormatsRepository(JdbcTemplate jdbc) {
        template = jdbc;
    }

    public List<AvailableFormats> findFormatsByWorkId(long workId) {
        return template.query("SELECT * FROM FORMATS WHERE WORK_ID = ?", newAvailableFormatsMapper(), workId);
    }

    @Override
    public AvailableFormats create(AvailableFormats format) {
        int update = template.update(
                "INSERT INTO FORMATS (WORK_ID, FORMAT, SHIPPING_COST) VALUES (?, ?, ?)",
                format.getWorkId(), format.getWorkFormat().ordinal(), format.getShippingCost());
        return (update == 0) ? null
                : template.queryForObject(
                        "SELECT * FROM FORMATS WHERE WORK_ID = ? AND FORMAT = ?",
                        newAvailableFormatsMapper(),
                        format.getWorkId(), format.getWorkFormat().ordinal());
    }

    @Override
    public List<AvailableFormats> findAll() {
        return template.query(
                "SELECT * FROM FORMATS", newAvailableFormatsMapper());
    }

    @Override
    public AvailableFormats findById(long id) {
        return template.queryForObject(
                "SELECT * FROM FORMATS WHERE ID = ?", newAvailableFormatsMapper(), id);
    }

    @Override
    public AvailableFormats update(AvailableFormats format) {
        int update = template.update(
                "UPDATE FORMATS SET WORK_ID = ?, FORMAT = ?, SHIPPING_COST = ? WHERE ID = ?",
                format.getWorkId(), format.getWorkFormat().ordinal(), format.getShippingCost(), format.getId());
        return (update == 0) ? null : format;

    }

    @Override
    public void delete(AvailableFormats format) {
        template.update("DELETE FROM FORMATS WHERE ID = ?", format.getId());
    }


    protected void syncAvailableFormats(Work work) {
        Set<AvailableFormats> formats = work.getFormats().stream()
                .map(f -> {
                    f.setWorkId(work.getId());
                    return save(f);
                })
                .collect(Collectors.toSet());
        work.setFormats(formats);
    }
}
