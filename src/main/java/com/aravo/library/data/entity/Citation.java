package com.aravo.library.data.entity;

import com.aravo.library.data.Persistable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(NON_NULL)
public class Citation implements Serializable, Persistable {
    private long id;
    private long workId;
    private int pageNumber;
    private String citedWork;
    private String citationAuthor;
    private LocalDate citedOn;

    public Citation(int page, String work, String author, LocalDate when) {
        setPageNumber(page);
        setCitedWork(work);
        setCitationAuthor(author);
        setCitedOn(when);
    }
}
