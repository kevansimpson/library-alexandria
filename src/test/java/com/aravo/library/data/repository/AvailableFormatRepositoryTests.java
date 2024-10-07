package com.aravo.library.data.repository;

import com.aravo.library.data.entity.AvailableFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * There are 10 formats across 9 works in seed data.
 * @see com.aravo.library.config.LibraryConfiguration#loadSeedData(AuthorRepository, WorkRepository)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AvailableFormatRepositoryTests {

    @Autowired
    private AvailableFormatRepository repository;

    @Test @DirtiesContext
    void testAvailableFormatRepository() {
        List<AvailableFormat> formats = repository.findAll();
        assertThat(formats).isNotNull();
        assertThat(formats.size()).isEqualTo(10);
        assertThat(repository.delete(1)).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(9);

        // missing format
        assertThat(repository.findById(11)).isNull();
        assertThat(repository.delete(11)).isFalse();
    }
}
