package com.aravo.library.data.repository;

import com.aravo.library.data.entity.VolumeInfo;
import com.aravo.library.data.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aravo.library.data.repository.EntityRowMappers.newVolumeInfoMapper;

@Component
public class VolumeRepository implements CrudRepository<VolumeInfo>{

    private final JdbcTemplate template;

    @Autowired
    public VolumeRepository(JdbcTemplate jdbc) {
        template = jdbc;
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

    @Override
    public VolumeInfo create(VolumeInfo info) {
        int update = template.update(
                "INSERT INTO VOLUMES (WORK_ID, VOLUME_NUMBER, SERIES_TITLE) VALUES (?, ?, ?)",
                info.getWorkId(), info.getVolume(), info.getSeriesTitle());
        return (update == 0) ? null
                : template.queryForObject("SELECT * FROM VOLUMES WHERE WORK_ID = ? AND VOLUME_NUMBER = ?",
                newVolumeInfoMapper(), info.getWorkId(), info.getVolume());
    }

    @Override
    public List<VolumeInfo> findAll() {
        return template.query(
                "SELECT * FROM VOLUMES ORDER BY WORK_ID, VOLUME_NUMBER", newVolumeInfoMapper());
    }

    @Override
    public VolumeInfo findById(long id) {
        try {
            return template.queryForObject(
                    "SELECT * FROM VOLUMES WHERE ID = ?", newVolumeInfoMapper(), id);
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
    public void delete(VolumeInfo info) {
        template.update("DELETE FROM VOLUMES WHERE ID = ?", info.getId());
    }

    protected void syncVolumeInfo(Work work) {
        VolumeInfo info = work.getVolumeInfo();
        if (info != null) {
            info.setWorkId(work.getId());
            work.setVolumeInfo(save(info));
        }
        else
            work.setVolumeInfo(findVolumeInfoByWorkId(work.getId()));
    }
}
