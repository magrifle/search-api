package com.mightwork.searchapi;

import com.mightwork.searchapi.data.SearchKey;
import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.List;

public abstract class SearchConfigurer<T>
{

    public abstract List<SearchKey> getSearchKeys();


    public SimpleDateFormat getDateKeyFormat()
    {
        return new SimpleDateFormat("yyyy-MM-dd");
    }


    public final Class<T> getType()
    {
        return (Class<T>) ((ParameterizedType) this.getClass()
            .getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
