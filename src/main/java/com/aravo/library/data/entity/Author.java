package com.aravo.library.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "AUTHORS")
public class Author implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(length=50, nullable=false)
    private String firstName;

    @Column(length=50, nullable=false)
    private String lastName;
}
