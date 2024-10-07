package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Citation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * There is 1 citation across 9 works in seed data.
 * @see com.aravo.library.config.LibraryConfiguration#loadSeedData(AuthorRepository, WorkRepository)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CitationRepositoryTests {

    @Autowired
    private CitationRepository repository;

    @Test @DirtiesContext
    void testCitationRepository() {
        List<Citation> citations = repository.findAll();
        assertThat(citations).isNotNull();
        assertThat(citations.size()).isEqualTo(1);
        assertThat(repository.delete(1)).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(0);

        // missing citation
        assertThat(repository.findById(2)).isNull();
        assertThat(repository.delete(2)).isFalse();
    }
}
