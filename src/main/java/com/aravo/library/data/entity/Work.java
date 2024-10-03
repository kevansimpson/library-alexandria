package com.aravo.library.data.entity;

import com.aravo.library.data.Persistable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(NON_NULL)
public class Work implements Serializable, Persistable {
    private long id;
    private String title;
    private Date published;
    private boolean rare;
    private Set<Author> authors = new HashSet<>();
    private Set<Citation> citations = new HashSet<>();
    private Set<AvailableFormat> formats = new HashSet<>();
    private Forward forward;
    private VolumeInfo volumeInfo;

    public Work(String title, Date publishedOn, boolean isRare) {
        setTitle(title);
        setPublished(publishedOn);
        setRare(isRare);
    }

    public Work(long id, String title, Date publishedOn, boolean isRare) {
        this(title, publishedOn, isRare);
        setId(id);
    }

    public void addAuthor(Author author) {
        authors.add(author);
    }

    public void removeAuthor(long authorId) {
        Author author = authors.stream().filter(a -> a.getId() == authorId).findFirst().orElse(null);
        if (author != null) {
            authors.remove(author);
        }
    }

    public void addCitation(Citation citation) {
        citations.add(citation);
        citation.setWorkId(getId());
    }

    public void removeCitation(long citationId) {
        Citation citation = citations.stream().filter(a -> a.getId() == citationId).findFirst().orElse(null);
        if (citation != null) {
            citations.remove(citation);
        }
    }

    public void addFormat(AvailableFormat format) {
        formats.add(format);
        format.setWorkId(getId());
    }

    public void removeFormat(long formatId) {
        AvailableFormat format = formats.stream().filter(a -> a.getId() == formatId).findFirst().orElse(null);
        if (format != null) {
            formats.remove(format);
        }
    }

    public void setForward(Forward newForward) {
        forward = newForward;
        if (forward != null)
            forward.setWorkId(getId());
    }

    public void setVolumeInfo(VolumeInfo info) {
        volumeInfo = info;
        if (volumeInfo != null)
            volumeInfo.setWorkId(getId());
    }
}
