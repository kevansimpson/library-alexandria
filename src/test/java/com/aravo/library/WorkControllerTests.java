package com.aravo.library;

import com.aravo.library.controller.WorkController;
import com.aravo.library.data.WorkFormat;
import com.aravo.library.data.entity.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"rawtypes"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class WorkControllerTests {

    @Autowired
    private WorkController workController;

    private final ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(workController).isNotNull();
    }

    @Test @DirtiesContext
    void testWorksFetchAll() {
        ResponseEntity<List> entity = restTemplate.getForEntity(workUrl(), List.class);
        assertThat(entity).isNotNull();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Work> works = objectMapper.convertValue(entity.getBody(), new TypeReference<>() {});
        assertThat(works).isNotNull();
        assertThat(works).isNotEmpty();
        assertThat(works.size()).isEqualTo(9);
        Map<String, Work> workMap = new TreeMap<>(works.stream().collect(
                Collectors.toMap(Work::getTitle, Function.identity())));
        assertThat(workMap.keySet()).isEqualTo(Set.of(
                "Demon Haunted World", "Dragon Wing", "Elven Star",
                "Fire Sea", "Into The Labyrinth", "Patterns of Enterprise Application Architecture",
                "Serpent Mage", "The Hand of Chaos", "The Seventh Gate"));
        Work seventhGate = workMap.get("The Seventh Gate");
        assertThat(seventhGate.getAuthors().size()).isEqualTo(2);
        assertThat(seventhGate.getCitations()).isEqualTo(
                Set.of(new Citation(1, seventhGate.getId(),
                        341, "Fire Sea, vol. 3 of The Death Gate Cycle",
                        "M. Weis", LocalDate.of(1994, 8, 31))));
        assertThat(seventhGate.getFormats()).isEqualTo(
                Set.of(new AvailableFormat(8, seventhGate.getId(), WorkFormat.SCROLL, BigDecimal.valueOf(5.99))));
        assertThat(seventhGate.getForward()).isNull();
        assertThat(seventhGate.getVolumeInfo()).isEqualTo(
                new VolumeInfo(7, seventhGate.getId(),7, "The Death Gate Cycle"));
    }

    @Test @DirtiesContext
    void testCrudEndpoints() {
        ResponseEntity<Work> find1 = restTemplate.getForEntity(workUrl() + "/10", Work.class);
        assertThat(find1).isNotNull();
        assertThat(find1.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        // create
        Work newWork = new Work("Made Up Book", LocalDate.of(2024, 10, 1), false);
        Citation citation = new Citation(
                1138, "Fake Citation",
                "K. Simpson", LocalDate.of(1999, 12, 31));
        newWork.addCitation(citation);
        AvailableFormat format = new AvailableFormat(WorkFormat.SCROLL, BigDecimal.valueOf(0.99));
        newWork.addFormat(format);
        Forward forward = new Forward("Martin Fowler", "This book is amazeballs!");
        newWork.setForward(forward);
        VolumeInfo volume = new VolumeInfo(1, "There Can Be Only One");
        newWork.setVolumeInfo(volume);

        ResponseEntity<Work> create = restTemplate.postForEntity(workUrl(), newWork, Work.class);
        assertThat(create).isNotNull();
        assertThat(create.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Work createdWork = objectMapper.convertValue(create.getBody(), new TypeReference<>() {});
        verifyNewWork(createdWork, "");
        // read
        ResponseEntity<Work> find2 = restTemplate.getForEntity(workUrl() + "/10", Work.class);
        assertThat(find2).isNotNull();
        assertThat(find2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Work read = objectMapper.convertValue(find2.getBody(), new TypeReference<>() {});
        verifyNewWork(read, "");
        // update
        read.setTitle("Made Up Book: No Really, It's Fake");
        restTemplate.put(workUrl(), read);
        ResponseEntity<Work> find3 = restTemplate.getForEntity(workUrl() + "/10", Work.class);
        assertThat(find3).isNotNull();
        assertThat(find3.getStatusCode()).isEqualTo(HttpStatus.OK);
        Work updated = objectMapper.convertValue(find3.getBody(), new TypeReference<>() {});
        verifyNewWork(updated, ": No Really, It's Fake");
        verifyCitation(updated, citation);
        verifyAvailableFormat(updated, format);
        verifyForward(updated, forward);
        verifyVolume(updated, volume);

        // delete
        restTemplate.delete(workUrl() + "/10");
        ResponseEntity<Work> findNone = restTemplate.getForEntity(workUrl() + "/10", Work.class);
        assertThat(findNone).isNotNull();
        assertThat(findNone.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }


    @Test @DirtiesContext
    void testCannotDeleteMissingWork() {
        ResponseEntity<Work> entity = workController.deleteWork(1000);
        assertThat(entity).isNotNull();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

     private void verifyNewWork(Work madeUp, String appended) {
        assertThat(madeUp).isNotNull();
        assertThat(madeUp.getId()).isEqualTo(10);
        assertThat(madeUp.getTitle()).isEqualTo("Made Up Book" + appended);
        assertThat(madeUp.getPublished()).isEqualTo(LocalDate.of(2024, 10, 1));
        assertThat(madeUp.isRare()).isEqualTo(false);
    }

    private void verifyCitation(Work updated, Citation citation) {
        assertThat(citation.getId()).isEqualTo(0);
        assertThat(citation.getWorkId()).isEqualTo(0);
        citation.setId(2); // seed data contains 1 citation
        citation.setWorkId(10);
        assertThat(updated.getCitations().size()).isEqualTo(1);
        assertThat(updated.getCitations()).isEqualTo(Set.of(citation));
    }

    private void verifyAvailableFormat(Work updated, AvailableFormat format) {
        assertThat(format.getId()).isEqualTo(0);
        assertThat(format.getWorkId()).isEqualTo(0);
        format.setId(11); // seed data contains 10 formats
        format.setWorkId(10);
        assertThat(updated.getFormats().size()).isEqualTo(1);
        assertThat(updated.getFormats()).isEqualTo(Set.of(format));
    }

    private void verifyForward(Work updated, Forward forward) {
        assertThat(forward.getId()).isEqualTo(0);
        assertThat(forward.getWorkId()).isEqualTo(0);
        forward.setId(2); // seed data contains 1 forward
        forward.setWorkId(10);
        assertThat(updated.getForward()).isNotNull();
        assertThat(updated.getForward()).isEqualTo(forward);
    }

    private void verifyVolume(Work updated, VolumeInfo volume) {
        assertThat(volume.getId()).isEqualTo(0);
        assertThat(volume.getWorkId()).isEqualTo(0);
        volume.setId(8); // seed data contains 7 volumes
        volume.setWorkId(10);
        assertThat(updated.getVolumeInfo()).isNotNull();
        assertThat(updated.getVolumeInfo()).isEqualTo(volume);
    }

    private String workUrl() {
        return "http://localhost:" + port + "/work";
    }
}
