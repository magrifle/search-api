package com.mightwork.searchapi;

import com.mightwork.searchapi.data.SearchKey;

import java.lang.reflect.ParameterizedType;
import java.text.SimpleDateFormat;
import java.util.List;

public abstract class SearchConfigurer<T> {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public abstract List<SearchKey> getSearchKeys();


    public SimpleDateFormat getDateKeyFormat() {
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT);
    }


    public final Class<T> getType() {
        return (Class<T>) ((ParameterizedType) this.getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
