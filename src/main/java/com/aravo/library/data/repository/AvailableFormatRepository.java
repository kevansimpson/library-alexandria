package com.aravo.library.data.repository;

import com.aravo.library.data.entity.AvailableFormat;
import com.aravo.library.data.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aravo.library.data.repository.EntityRowMappers.newAvailableFormatMapper;

@Component
public class AvailableFormatRepository implements CrudRepository<AvailableFormat> {

    private final JdbcTemplate template;

    @Autowired
    public AvailableFormatRepository(JdbcTemplate jdbc) {
        template = jdbc;
    }

    public List<AvailableFormat> findFormatsByWorkId(long workId) {
        return template.query("SELECT * FROM FORMATS WHERE WORK_ID = ?", newAvailableFormatMapper(), workId);
    }

    @Override
    public AvailableFormat create(AvailableFormat format) {
        int update = template.update(
                "INSERT INTO FORMATS (WORK_ID, FORMAT, SHIPPING_COST) VALUES (?, ?, ?)",
                format.getWorkId(), format.getWorkFormat().ordinal(), format.getShippingCost());
        return (update == 0) ? null
                : template.queryForObject(
                        "SELECT * FROM FORMATS WHERE WORK_ID = ? AND FORMAT = ?",
                        newAvailableFormatMapper(),
                        format.getWorkId(), format.getWorkFormat().ordinal());
    }

    @Override
    public List<AvailableFormat> findAll() {
        return template.query(
                "SELECT * FROM FORMATS", newAvailableFormatMapper());
    }

    @Override
    public AvailableFormat findById(long id) {
        return template.queryForObject(
                "SELECT * FROM FORMATS WHERE ID = ?", newAvailableFormatMapper(), id);
    }

    @Override
    public AvailableFormat update(AvailableFormat format) {
        int update = template.update(
                "UPDATE FORMATS SET WORK_ID = ?, FORMAT = ?, SHIPPING_COST = ? WHERE ID = ?",
                format.getWorkId(), format.getWorkFormat().ordinal(), format.getShippingCost(), format.getId());
        return (update == 0) ? null : format;

    }

    @Override
    public void delete(AvailableFormat format) {
        template.update("DELETE FROM FORMATS WHERE ID = ?", format.getId());
    }


    protected void syncAvailableFormats(Work work) {
        Set<AvailableFormat> formats = work.getFormats().stream()
                .map(f -> {
                    f.setWorkId(work.getId());
                    return save(f);
                })
                .collect(Collectors.toSet());
        work.setFormats(formats);
    }
}
