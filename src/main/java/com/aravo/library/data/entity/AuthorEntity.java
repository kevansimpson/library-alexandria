package com.aravo.library.data.entity;

import com.aravo.library.data.Author;
import jakarta.persistence.*;

@SqlResultSetMapping(
        name = "AuthorRecordMapping",
        classes = @ConstructorResult(
                targetClass = Author.class,
                columns = {
                        @ColumnResult(name = "id", type = Long.class),
                        @ColumnResult(name = "name", type = String.class)
                }))
@Entity
@Table(name = "AUTHORS")
public class AuthorEntity {
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    private Long id;

    @Column//(name="AUTHOR_NAME", length=100, nullable=false)
    private String name;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
