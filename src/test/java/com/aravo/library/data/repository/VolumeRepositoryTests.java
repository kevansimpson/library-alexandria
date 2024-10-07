package com.aravo.library.data.repository;

import com.aravo.library.data.entity.VolumeInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * There are 7 Death Gate Cycle volumes in seed data.
 * @see com.aravo.library.config.LibraryConfiguration#loadSeedData(AuthorRepository, WorkRepository)
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VolumeRepositoryTests {

    @Autowired
    private VolumeRepository repository;

    @Test @DirtiesContext
    void testVolumeRepository() {
        List<VolumeInfo> volumes = repository.findAll();
        assertThat(volumes).isNotNull();
        assertThat(volumes.size()).isEqualTo(7);
        for (int vol = 1; vol <= 7; vol++) {
            VolumeInfo info = volumes.get(vol - 1);
            assertThat(info.getId()).isEqualTo(vol);
            assertThat(info.getVolume()).isEqualTo(vol);
            assertThat(info.getSeriesTitle()).isEqualTo("The Death Gate Cycle");
        }
        assertThat(repository.delete(1)).isTrue();
        assertThat(repository.findAll().size()).isEqualTo(6);

        // missing volume
        assertThat(repository.findById(8)).isNull();
        assertThat(repository.delete(8)).isFalse();
    }
}
