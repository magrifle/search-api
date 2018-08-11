package com.mightwork.searchapi.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchApi {

    String queryString() default "q";

    char keySeparator() default ',';

    Class entity();

    boolean failOnMissingQueryString() default false;

}
