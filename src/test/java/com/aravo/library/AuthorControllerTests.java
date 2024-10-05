package com.aravo.library;

import com.aravo.library.controller.AuthorController;
import com.aravo.library.data.entity.Author;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings({"rawtypes"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class AuthorControllerTests {

    @Autowired
    private AuthorController authorController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertThat(authorController).isNotNull();
    }

    @Test @DirtiesContext
    void testAuthorFetchAll() {
        ResponseEntity<List> entity = restTemplate.getForEntity(authorUrl(), List.class);
        assertThat(entity).isNotNull();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Author> authors = objectMapper.convertValue(entity.getBody(), new TypeReference<>() {});
        assertThat(authors).isNotNull();
        assertThat(authors).isNotEmpty();
        assertThat(authors.size()).isEqualTo(4);
        List<String> fullNames = authors.stream().map(a -> a.getFirstName() +" "+ a.getLastName()).toList();
        assertThat(fullNames).isEqualTo(List.of("Martin Fowler", "Tracy Hickman", "Carl Sagan", "Margaret Weis"));
    }

    @Test @DirtiesContext
    void testCrudEndpoints() {
        ResponseEntity<Author> find1 = restTemplate.getForEntity(authorUrl() + "/5", Author.class);
        assertThat(find1).isNotNull();
        assertThat(find1.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        // create
        ResponseEntity<Author> create = restTemplate.postForEntity(
                authorUrl(), new Author("Kevan", "Simpson"), Author.class);
        assertThat(create).isNotNull();
        assertThat(create.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Author kevan1 = objectMapper.convertValue(create.getBody(), new TypeReference<>() {});
        verifyNewAuthor(kevan1, "");
        // read
        ResponseEntity<Author> find2 = restTemplate.getForEntity(authorUrl() + "/5", Author.class);
        assertThat(find2).isNotNull();
        assertThat(find2.getStatusCode()).isEqualTo(HttpStatus.OK);
        Author kevan2 = objectMapper.convertValue(find2.getBody(), new TypeReference<>() {});
        verifyNewAuthor(kevan2, "");
        // update
        restTemplate.put(authorUrl(), new Author(5, "KevanUpdated", "Simpson"));
        ResponseEntity<Author> find3 = restTemplate.getForEntity(authorUrl() + "/5", Author.class);
        assertThat(find3).isNotNull();
        assertThat(find3.getStatusCode()).isEqualTo(HttpStatus.OK);
        Author kevan3 = objectMapper.convertValue(find3.getBody(), new TypeReference<>() {});
        verifyNewAuthor(kevan3, "Updated");
        // delete
        restTemplate.delete(authorUrl() + "/5");
        ResponseEntity<Author> findNone = restTemplate.getForEntity(authorUrl() + "/5", Author.class);
        assertThat(findNone).isNotNull();
        assertThat(findNone.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private void verifyNewAuthor(Author kevan, String appended) {
        assertThat(kevan).isNotNull();
        assertThat(kevan.getFirstName()).isEqualTo("Kevan" + appended);
        assertThat(kevan.getLastName()).isEqualTo("Simpson");
        assertThat(kevan.getId()).isEqualTo(5);
    }

    private String authorUrl() {
        return "http://localhost:" + port + "/author";
    }
}
