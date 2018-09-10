package com.github.magrifle.data.searchapi.aspect;

import com.github.magrifle.data.searchapi.SearchConfigurer;
import com.github.magrifle.data.searchapi.SearchKeyConfigurerService;
import com.github.magrifle.data.searchapi.SearchOperation;
import com.github.magrifle.data.searchapi.annotation.SearchApi;
import com.github.magrifle.data.searchapi.data.SearchBuilder;
import com.github.magrifle.data.searchapi.exception.SearchApiConfigurationException;
import com.github.magrifle.data.searchapi.exception.SearchKeyValidationException;
import com.github.magrifle.data.searchapi.specification.EntitySpecification;
import com.github.magrifle.data.searchapi.specification.SpecificationsBuilder;
import com.google.common.base.Joiner;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Aspect
public class DataSearchApi {
    private static final Logger logger = LoggerFactory.getLogger(DataSearchApi.class);

    @Autowired
    private List<SearchConfigurer> searchConfigurers;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    //could expose this config if startup is significantly slow
    private boolean checkConfigurationsOnStartup = true;


    @Pointcut("@annotation(searchApi)")
    public void methodsWithSearchApi(SearchApi searchApi) {
    }


    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) ||" +
            "within(@org.springframework.stereotype.Controller *)")
    public void allPublicControllerMethodsPointcut() {
    }


    @Around("allPublicControllerMethodsPointcut() && methodsWithSearchApi(searchApi)")
    public Object buildSpecificationForMethod(ProceedingJoinPoint joinPoint, SearchApi searchApi) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String queryParameter = request.getParameter(searchApi.queryString());
        if ((queryParameter == null || queryParameter.isEmpty()) && searchApi.failOnMissingQueryString()) {
            throw new SearchKeyValidationException("The required query parameter \"" + searchApi.queryString() + "\" is missing");
        }
        logger.debug("Got query string {}", queryParameter);
        String join = Joiner.on("|").join(SearchOperation.SIMPLE_OPERATION_SET.keySet());
        Pattern pattern = Pattern.compile("(\\w+?)(" + join + ")(\\*?)([\\w-]+?)(\\*?),");
        Matcher matcher = pattern.matcher(queryParameter + searchApi.keySeparator().getValue());
        Object[] args = joinPoint.getArgs();

        SearchConfigurer first = searchConfigurers.stream()
                .filter(b -> b.getType() == searchApi.entity())
                .findFirst()
                .orElseThrow(() -> new SearchApiConfigurationException("Could not find a configuration bean of " + SearchConfigurer.class.getName() + "<\"" + searchApi.entity() + "\">"));

        //rework to use a single instance of all classes except the configurer
        SpecificationsBuilder builder = new SpecificationsBuilder(new SearchKeyConfigurerService(first));
        while (matcher.find()) {
            builder.with(
                    matcher.group(1),
                    matcher.group(2),
                    matcher.group(4),
                    matcher.group(3),
                    matcher.group(5)
            );
        }
        Specification build = builder.build();
        Specification specification = build == null ? new EntitySpecification<>() : build;
        IntStream.range(0, args.length).filter(i -> args[i].getClass() == SearchBuilder.class).findFirst().ifPresent(i -> args[i] = new SearchBuilder<>(specification));
        return joinPoint.proceed(args);
    }


    @PostConstruct
    public void init() {
        if (this.checkConfigurationsOnStartup) {
            this.checkBeanConfigurations();
        }

    }


    private void checkBeanConfigurations() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods =
                this.requestMappingHandlerMapping.getHandlerMethods();

        for (HandlerMethod handlerMethod : handlerMethods.values()) {
            SearchApi methodAnnotation = handlerMethod.getMethodAnnotation(SearchApi.class);
            if (methodAnnotation != null) {
                Class type = methodAnnotation.entity();
                searchConfigurers.stream()
                        .filter(b -> b.getType() == type)
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("You have defined @" + SearchApi.class.getName() + "(entity=\"" + type + "\".class on method [" + handlerMethod
                                .getMethod()
                                .getName() + "] but you have not created a bean of entity " + SearchConfigurer.class.getName() + "<\"" + type + "\">"));
            }
        }
    }


}
