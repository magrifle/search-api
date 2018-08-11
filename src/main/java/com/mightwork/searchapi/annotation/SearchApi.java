package com.mightwork.searchapi.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SearchApi {

    String queryString() default "q";

    char keySeparator() default ',';

    Class entity();

    boolean failOnMissingQueryString() default false;

}
