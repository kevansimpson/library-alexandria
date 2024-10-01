package com.aravo.library.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "AUTHORS")
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", length=50, nullable=false)
    private String firstName;

    @Column(name = "last_name", length=50, nullable=false)
    private String lastName;

    public Author(String first, String last) {
        firstName = first;
        lastName = last;
    }

    @ManyToMany(//fetch = FetchType.LAZY,
//            cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            mappedBy = "authors")
    @JsonIgnore
    private Set<Work> works = new HashSet<>();
}
