package com.aravo.library.data.repository;

import com.aravo.library.data.entity.Citation;
import com.aravo.library.data.entity.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.aravo.library.data.repository.EntityRowMappers.newCitationMapper;

@Component
public class CitationRepository implements CrudRepository<Citation> {

    private final JdbcTemplate template;

    @Autowired
    public CitationRepository(JdbcTemplate jdbc) {
        template = jdbc;
    }

    public List<Citation> findCitationsByWorkId(long workId) {
        return template.query("SELECT * FROM CITATIONS WHERE WORK_ID = ?", newCitationMapper(), workId);
    }

    @Override
    public Citation create(Citation citation) {
        int update = template.update(
                "INSERT INTO CITATIONS (WORK_ID, PAGE_NUMBER, CITED_WORK, CITATION_AUTHOR, CITED_WHEN) VALUES (?, ?, ?, ?, ?)",
                citation.getWorkId(), citation.getPageNumber(),
                citation.getCitedWork(), citation.getCitationAuthor(), citation.getCitedOn());
        return (update == 0) ? null
                : template.queryForObject(
                        "SELECT * FROM CITATIONS WHERE WORK_ID = ? AND PAGE_NUMBER = ? AND CITED_WORK = ?",
                        newCitationMapper(),
                        citation.getWorkId(), citation.getPageNumber(), citation.getCitedWork());
    }

    @Override
    public List<Citation> findAll() {
        return template.query("SELECT * FROM CITATIONS", newCitationMapper());
    }

    @Override
    public Citation findById(long id) {
        return template.queryForObject("SELECT * FROM CITATIONS WHERE ID = ?", newCitationMapper(), id);
    }

    @Override
    public Citation update(Citation citation) {
        int update = template.update(
                "UPDATE CITATIONS SET WORK_ID = ?, PAGE_NUMBER = ?, CITED_WORK = ?, CITATION_AUTHOR = ?, CITED_WHEN = ? WHERE ID = ?",
                citation.getWorkId(), citation.getPageNumber(), citation.getCitedWork(),
                citation.getCitationAuthor(), citation.getCitedOn(), citation.getId());
        return (update == 0) ? null : citation;

    }

    @Override
    public void delete(Citation citation) {
        template.update("DELETE FROM CITATIONS WHERE ID = ?", citation.getId());
    }

    protected void syncCitation(Work work) {
        Set<Citation> citations = work.getCitations().stream()
                .map(c -> {
                    c.setWorkId(work.getId());
                    return save(c);
                })
                .collect(Collectors.toSet());
        work.setCitations(citations);
    }
}
