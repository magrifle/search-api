package com.github.magrifle.data.searchapi.test_app.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "many_entity")
public class ManyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
