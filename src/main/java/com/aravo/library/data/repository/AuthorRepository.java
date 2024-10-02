package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class AuthorRepository implements CrudRepository<Author>{

    private final JdbcTemplate template;

    @Autowired
    public AuthorRepository(JdbcTemplate jdbc) {
        template = jdbc;
    }

    @Override
    public List<Author> findAll() {
        return template.query(
                "SELECT * FROM AUTHORS ORDER BY LAST_NAME, FIRST_NAME", new AuthorMapper());
    }

    @Override
    public Author findById(long id) {
        return template.queryForObject(
                "SELECT * FROM AUTHORS WHERE ID = ?", new AuthorMapper(), id);
    }

    @Override
    public Author save(Author author) {
        int update = template.update(
                "INSERT INTO AUTHORS (FIRST_NAME, LAST_NAME) VALUES (?, ?)",
                author.getFirstName(), author.getLastName());
        return (update == 0) ? null
                : template.queryForObject("SELECT * FROM AUTHORS WHERE FIRST_NAME = ? AND LAST_NAME = ?",
                new AuthorMapper(), author.getFirstName(), author.getLastName());
    }

    @Override
    public void delete(Author author) {
        template.update("DELETE FROM AUTHORS WHERE ID = ?", author.getId());
    }

    private static class AuthorMapper implements RowMapper<Author> {
        @Override
        public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Author(
                    rs.getInt("ID"),
                    rs.getString("FIRST_NAME"),
                    rs.getString("LAST_NAME"));
        }
    }
}
