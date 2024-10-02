package com.aravo.library.data.entity;

import com.aravo.library.data.Persistable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Author implements Serializable, Persistable {
    private long id;
    private String firstName;
    private String lastName;

    public Author(String first, String last) {
        setFirstName(first);
        setLastName(last);
    }
}
