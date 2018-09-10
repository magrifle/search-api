package com.github.magrifle.data.searchapi.annotation;

import com.github.magrifle.data.searchapi.specification.SearchParameterSeparator;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchApi {

    String queryString() default "q";

    SearchParameterSeparator keySeparator() default SearchParameterSeparator.COMMA;

    Class entity();

    boolean failOnMissingQueryString() default false;

}
