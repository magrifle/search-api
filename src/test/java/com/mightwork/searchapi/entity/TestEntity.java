package com.mightwork.searchapi.entity;

import java.util.Date;

public class TestEntity
{
    private String name;

    private String age;

    private Date dateCreated;


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getAge()
    {
        return age;
    }


    public void setAge(String age)
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