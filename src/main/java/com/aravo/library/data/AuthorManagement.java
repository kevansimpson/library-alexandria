package com.aravo.library.data;

import com.aravo.library.data.entity.Author;
import com.aravo.library.data.repository.AuthorRepository;
import com.aravo.library.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthorManagement implements AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Author createAuthor(Author author) {
        long newId = fetchNextId();
        jdbcTemplate.update(
                "INSERT INTO AUTHORS VALUES (?, ?, ?)",
                newId, author.getFirstName(), author.getLastName());
        return authorRepository.findById(newId).orElse(null);
    }

    @SuppressWarnings("DataFlowIssue")
    long fetchNextId() {
        return jdbcTemplate.queryForObject("SELECT MAX(id) FROM AUTHORS", Long.class) + 1L;
    }

    public List<Author> fetchAuthors() {
        List<Author> authors = new ArrayList<>();
        for (Author a : authorRepository.findAll())
            authors.add(a);
        return authors;
    }

    public Author updateAuthor(Author author) {
        int update = jdbcTemplate.update(
                "UPDATE AUTHORS SET first_name = ?, last_name = ? WHERE id = ?",
                author.getFirstName(), author.getLastName(), author.getId());
        if (update > 0)
            return authorRepository.findById(author.getId()).orElse(null);
        else
            return createAuthor(author);
    }


    public Author deleteAuthor(Author author) {
        authorRepository.delete(author);
        return authorRepository.findById(author.getId()).orElse(null);
    }

}
