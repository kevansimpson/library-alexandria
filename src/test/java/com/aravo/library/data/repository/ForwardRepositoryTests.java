package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Forward;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * There is 1 forward across 9 works in seed data.
 * @see com.aravo.library.config.LibraryConfiguration#loadSeedData(AuthorRepository, WorkRepository)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForwardRepositoryTests {

    @Autowired
    private ForwardRepository repository;

    @Test @DirtiesContext
    void testForwardRepository() {
        List<Forward> forwards = repository.findAll();
        assertThat(forwards).isNotNull();
        assertThat(forwards.size()).isEqualTo(1);
        assertThat(repository.delete(1)).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(0);

        // missing forward
        assertThat(repository.findById(2)).isNull();
        assertThat(repository.delete(2)).isFalse();
    }
}
