package com.aravo.library.data.entity;


import com.aravo.library.data.Persistable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Forward implements Serializable, Persistable {
    private long id;
    private long workId;
    private String author;
    private String forwardText;

    public Forward(String writer, String text) {
        setAuthor(writer);
        setForwardText(text);
    }
}
