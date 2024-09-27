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
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    private Long id;

    @Column(length=100, nullable=false)
    private String name;
}
