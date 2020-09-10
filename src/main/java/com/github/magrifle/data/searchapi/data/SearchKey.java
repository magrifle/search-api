package com.github.magrifle.data.searchapi.data;

import com.github.magrifle.data.searchapi.SearchOperation;
import java.util.function.Function;

public class SearchKey
{
    private String name;

    private SearchOperation[] deniedOperations;

    private String fieldName;

    private boolean required;

    private Function<String, Object> customization;


    public SearchKey(String name, String fieldName, boolean required)
    {
        this(name, fieldName);
        this.required = required;
    }


    public SearchKey(String name, String fieldName, boolean required, Function<String, Object> customization)
    {
        this(name, fieldName, required);
        this.customization = customization;
    }


    public SearchKey(String name, String fieldName, Function<String, Object> customization)
    {
        this(name, fieldName);
        this.customization = customization;
    }


    public SearchKey(String name, String fieldName)
    {
        this.name = name;
        this.fieldName = fieldName;
    }


    public SearchKey(String name, Function<String, Object> customization)
    {
        this.name = name;
        this.customization = customization;
    }


    public SearchKey(String fieldName)
    {
        this.name = fieldName;
        this.fieldName = fieldName;
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


    public Function<String, Object> getCustomization()
    {
        return customization;
    }


    public void setCustomization(Function<String, Object> customization)
    {
        this.customization = customization;
    }
}
