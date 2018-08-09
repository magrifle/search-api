package com.mightwork.searchapi.data;

import com.mightwork.searchapi.SearchOperation;

public class SearchKey
{
    private String name;

    private SearchOperation[] deniedOperations;

    private boolean dateField;

    private String fieldName;

    private boolean required;


    public SearchKey(String name, String fieldName)
    {
        this.name = name;
        this.fieldName = fieldName;
    }


    public SearchKey(String name, String fieldName, boolean isDateField)
    {
        this.name = name;
        this.fieldName = fieldName;
        this.dateField = isDateField;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public SearchOperation[] getDeniedOperations()
    {
        return deniedOperations;
    }


    public void setDeniedOperations(SearchOperation[] deniedOperations)
    {
        this.deniedOperations = deniedOperations;
    }


    public boolean isDateField()
    {
        return dateField;
    }


    public void setDateField(boolean dateField)
    {
        this.dateField = dateField;
    }


    public String getFieldName()
    {
        return fieldName;
    }


    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }


    public boolean isRequired()
    {
        return required;
    }


    public void setRequired(boolean required)
    {
        this.required = required;
    }
}
