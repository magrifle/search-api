package com.github.magrifle.data.searchapi.test_app.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Data
@Entity
@Table(name = "test_entity")
public class TestEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int age;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean human;

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
