package com.aravo.library.data.repository;

import com.aravo.library.data.WorkFormat;
import com.aravo.library.data.entity.*;
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

    public static CitationMapper newCitationMapper() {
        return new CitationMapper();
    }

    public static ForwardMapper newForwardMapper() {
        return new ForwardMapper();
    }

    public static VolumeInfoMapper newVolumeInfoMapper() {
        return new VolumeInfoMapper();
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

    private static class CitationMapper implements RowMapper<Citation> {
        @Override
        public Citation mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Citation(
                    rs.getInt("ID"),
                    rs.getInt("WORK_ID"),
                    rs.getInt("PAGE_NUMBER"),
                    rs.getString("CITED_WORK"),
                    rs.getString("CITATION_AUTHOR"),
                    rs.getDate("CITED_WHEN"));
        }
    }

    private static class ForwardMapper implements RowMapper<Forward> {
        @Override
        public Forward mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Forward(
                    rs.getInt("ID"),
                    rs.getInt("WORK_ID"),
                    rs.getString("AUTHOR"),
                    rs.getString("FORWARD"));
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

    private static class VolumeInfoMapper implements RowMapper<VolumeInfo> {
        @Override
        public VolumeInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new VolumeInfo(
                    rs.getInt("ID"),
                    rs.getInt("WORK_ID"),
                    rs.getInt("VOLUME_NUMBER"),
                    rs.getString("SERIES_TITLE"));
        }
    }
}
