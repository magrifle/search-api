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
public @interface SearchApi
{
    @AliasFor("value")
    String queryString() default "q";

    @AliasFor("queryString")
    String value() default "q";

    char keySeparator() default ',';

    Class type();

    boolean failOnMissingQueryString() default false;

}
