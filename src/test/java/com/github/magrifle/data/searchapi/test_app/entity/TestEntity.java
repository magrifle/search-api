package com.github.magrifle.data.searchapi.test_app.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "test_entity")
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int age;

    private Date dateCreated;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_child_entity"))
    private ChildEntity childEntity;

    @Fetch(FetchMode.SELECT)
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "fk_test_entity")),
            inverseJoinColumns = @JoinColumn(foreignKey = @ForeignKey(name = "fk_many_entities")))
    private List<ManyEntity> manyEntities;
}
