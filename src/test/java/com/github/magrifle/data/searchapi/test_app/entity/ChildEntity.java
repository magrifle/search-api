package com.github.magrifle.data.searchapi.test_app.entity;

import lombok.Data;

import javax.persistence.*;


@Data
@Table(name = "child_entity")
@Entity
public class ChildEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

}
