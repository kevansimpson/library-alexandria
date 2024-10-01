package com.aravo.library.data.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "WORKS")
public class Work implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(length=250, nullable=false)
    private String title;

    @Column(nullable=false)
    @Temporal(TemporalType.DATE)
    private Date published;

    @Column
    private boolean rare;

    @ManyToMany//(fetch = FetchType.LAZY,
//            cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "author_work_xref",
            joinColumns = { @JoinColumn(name = "work_id") },
            inverseJoinColumns = { @JoinColumn(name = "author_id") })
    private Set<Author> authors = new HashSet<>();

    public Work(String name, Date publishedOn, boolean isRare) {
        title = name;
        published = publishedOn;
        rare = isRare;
    }


    public void addAuthor(Author author) {
        authors.add(author);
        author.getWorks().add(this);
    }

    public void removeAuthor(long authorId) {
        Author author = authors.stream().filter(a -> a.getId() == authorId).findFirst().orElse(null);
        if (author != null) {
            authors.remove(author);
            author.getWorks().remove(this);
        }
    }

}
