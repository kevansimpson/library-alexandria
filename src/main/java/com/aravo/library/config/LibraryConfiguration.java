package com.aravo.library.config;

import com.aravo.library.data.entity.Author;
import com.aravo.library.data.repository.AuthorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LibraryConfiguration {
    @Bean
    public CommandLineRunner loadSeedData(AuthorRepository authorRepository) {
        return (args) -> {
            authorRepository.save(new Author("Margaret", "Weis"));
            authorRepository.save(new Author("Tracy", "Hickman"));
        };
    }
}
