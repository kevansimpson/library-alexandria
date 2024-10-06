package com.aravo.library.data.repository;

import com.aravo.library.data.entity.VolumeInfo;
import com.aravo.library.data.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Set;

import static com.aravo.library.data.repository.EntityRowMappers.newVolumeInfoMapper;

@Component
public class VolumeRepository implements CrudRepository<VolumeInfo>{

    private final JdbcTemplate template;
    private final OrphanRemover orphanRemover;

    @Autowired
    public VolumeRepository(JdbcTemplate jdbc, OrphanRemover remover) {
        template = jdbc;
        orphanRemover = remover;
    }

    public VolumeInfo findVolumeInfoByWorkId(long workId) {
        try {
            return template.queryForObject("SELECT * FROM VOLUMES WHERE WORK_ID = ?",
                    newVolumeInfoMapper(), workId);
        }
        catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    public void deleteVolumeInfoByWorkId(long workId) {
        template.update("DELETE FROM VOLUMES WHERE WORK_ID = ?", workId);
    }

    @Override
    public VolumeInfo create(VolumeInfo info) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO VOLUMES (WORK_ID, VOLUME_NUMBER, SERIES_TITLE) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, info.getWorkId());
            stmt.setInt(2, info.getVolume());
            stmt.setString(3, info.getSeriesTitle());
            return stmt;
        }, keyHolder);
        //noinspection DataFlowIssue
        return findById(keyHolder.getKeyAs(Long.class));
    }

    @Override
    public List<VolumeInfo> findAll() {
        return template.query(
                "SELECT * FROM VOLUMES ORDER BY WORK_ID, VOLUME_NUMBER", newVolumeInfoMapper());
    }

    @Override
    public VolumeInfo findById(long id) {
        try {
            return template.queryForObject("SELECT * FROM VOLUMES WHERE ID = ?", newVolumeInfoMapper(), id);
        }
        catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public VolumeInfo update(VolumeInfo info) {
        int update = template.update(
                "UPDATE VOLUMES SET WORK_ID = ?, VOLUME_NUMBER = ?, SERIES_TITLE WHERE ID = ?",
                info.getWorkId(), info.getVolume(), info.getSeriesTitle(), info.getId());
        return (update == 0) ? null : info;
    }

    @Override
    public void delete(long id) {
        template.update("DELETE FROM VOLUMES WHERE ID = ?", id);
    }

    protected void syncVolumeInfo(Work work) {
        VolumeInfo info = work.getVolumeInfo();
        if (info != null) {
            info.setWorkId(work.getId());
            work.setVolumeInfo(save(info));
            orphanRemover.removeOrphans(work, Set.of(work.getVolumeInfo()), "VOLUMES");
        }
        else
            orphanRemover.removeAllOrphans(work, "VOLUMES");
    }
}
