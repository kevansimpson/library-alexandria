package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Author;
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
import java.util.stream.Collectors;

import static com.aravo.library.data.repository.EntityRowMappers.newAuthorMapper;

@Component
public class AuthorRepository implements CrudRepository<Author>{

    private final JdbcTemplate template;
    private final OrphanRemover orphanRemover;

    @Autowired
    public AuthorRepository(JdbcTemplate jdbc, OrphanRemover remover) {
        template = jdbc;
        orphanRemover = remover;
    }

    public List<Author> findAuthorsByWorkId(long workId) {
        return template.query("SELECT DISTINCT a.* " +
                "FROM WORKS w " +
                "INNER JOIN AUTHOR_WORK_XREF x ON x.WORK_ID = ? " +
                "INNER JOIN AUTHORS a ON x.AUTHOR_ID = a.ID",
                newAuthorMapper(), workId);
    }

    public int countWorks(long authorId) {
        Integer exists = template.queryForObject(
                "SELECT count(*) FROM AUTHOR_WORK_XREF WHERE author_id = ?",
                Integer.class, authorId);
        return (exists == null) ? 0 : exists;
    }

    public void unlinkAuthorsByWorkId(long workId) {
        template.update("DELETE FROM AUTHOR_WORK_XREF WHERE WORK_ID = ?", workId);
    }

    @Override
    public Author create(Author author) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO AUTHORS (FIRST_NAME, LAST_NAME) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            return stmt;
        }, keyHolder);
        //noinspection DataFlowIssue
        return findById(keyHolder.getKeyAs(Long.class));
    }

    @Override
    public List<Author> findAll() {
        return template.query("SELECT * FROM AUTHORS ORDER BY LAST_NAME, FIRST_NAME", newAuthorMapper());
    }

    @Override
    public Author findById(long id) {
        try {
            return template.queryForObject("SELECT * FROM AUTHORS WHERE ID = ?", newAuthorMapper(), id);
        }
        catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public Author update(Author author) {
        int update = template.update(
                "UPDATE AUTHORS SET FIRST_NAME = ?, LAST_NAME = ? WHERE ID = ?",
                author.getFirstName(), author.getLastName(), author.getId());
        return (update == 0) ? null : author;
    }

    @Override
    public void delete(long id) {
        template.update("DELETE FROM AUTHORS WHERE ID = ?", id);
    }

    protected void syncAuthors(Work work) {
        Set<Author> authors = work.getAuthors().stream()
                .map(this::save).collect(Collectors.toSet());
        authors.forEach(a -> linkAuthorWork(work.getId(), a.getId()));
        orphanRemover.removeOrphans(work, authors, "AUTHOR_WORK_XREF", "author_id");
        work.setAuthors(authors);
    }

    protected void linkAuthorWork(long workId, long authorId) {
        Integer exists = template.queryForObject(
                "SELECT count(*) FROM AUTHOR_WORK_XREF WHERE work_id = ? AND author_id = ?",
                Integer.class, workId, authorId);
        if (exists == null || exists == 0)
            template.update(
                    "INSERT INTO AUTHOR_WORK_XREF (work_id, author_id) VALUES (?, ?)",
                    workId, authorId);
    }
}
