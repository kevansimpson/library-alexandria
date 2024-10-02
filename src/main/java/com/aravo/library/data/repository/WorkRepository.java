package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkRepository implements CrudRepository<Work> {

    private final JdbcTemplate template;

    @Autowired
    public WorkRepository(JdbcTemplate jdbc) {
        template = jdbc;
    }

    @Override
    public List<Work> findAll() {
        return new ArrayList<>();
    }

    @Override
    public Work findById(long id) {
        return null;
    }

    @Override
    public Work save(Work work) {
        return null;
    }

    @Override
    public void delete(Work work) {

    }

}
