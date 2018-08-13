package com.magrifle.data.searchapi.test_app.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "test_entity")
public class TestEntity
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private int age;

    private Date dateCreated;


    public TestEntity(String name, int age, Date dateCreated)
    {
        this.name = name;
        this.age = age;
        this.dateCreated = dateCreated;
    }


    public Long getId()
    {
        return id;
    }


    public void setId(Long id)
    {
        this.id = id;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public int getAge()
    {
        return age;
    }


    public void setAge(int age)
    {
        this.age = age;
    }


    public Date getDateCreated()
    {
        return dateCreated;
    }


    public void setDateCreated(Date dateCreated)
    {
        this.dateCreated = dateCreated;
    }
}