package com.aravo.library.data.repository;

import com.aravo.library.data.WorkFormat;
import com.aravo.library.data.entity.Author;
import com.aravo.library.data.entity.AvailableFormats;
import com.aravo.library.data.entity.Work;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntityRowMappers {
    public static AuthorMapper newAuthorMapper() {
        return new AuthorMapper();
    }

    public static AvailableFormatsMapper newAvailableFormatsMapper() {
        return new AvailableFormatsMapper();
    }

    public static WorkMapper newWorkMapper() {
        return new WorkMapper();
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

    private static class AvailableFormatsMapper implements RowMapper<AvailableFormats> {
        @Override
        public AvailableFormats mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AvailableFormats(
                    rs.getInt("ID"),
                    rs.getInt("WORK_ID"),
                    WorkFormat.values()[rs.getInt("FORMAT")],
                    rs.getBigDecimal("SHIPPING_COST"));
        }
    }

    private static class WorkMapper implements RowMapper<Work> {
        @Override
        public Work mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Work(
                    rs.getInt("ID"),
                    rs.getString("TITLE"),
                    rs.getDate("PUBLISHED"),
                    rs.getBoolean("RARE"));
        }
    }
}
