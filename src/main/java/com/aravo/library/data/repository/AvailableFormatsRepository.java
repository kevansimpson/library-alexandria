package com.aravo.library.data.repository;

import com.aravo.library.data.entity.AvailableFormats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AvailableFormatsRepository implements CrudRepository<AvailableFormats> {

    private final JdbcTemplate template;

    @Autowired
    public AvailableFormatsRepository(JdbcTemplate jdbc) {
        template = jdbc;
    }

    @Override
    public List<AvailableFormats> findAll() {
        return new ArrayList<>();
    }

    @Override
    public AvailableFormats findById(long id) {
        return null;
    }

    @Override
    public AvailableFormats save(AvailableFormats format) {
        return null;
    }

    @Override
    public void delete(AvailableFormats format) {

    }
}
