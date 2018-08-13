package com.mightwork.searchapi.test_app;

import com.mightwork.searchapi.SearchConfigurer;
import com.mightwork.searchapi.aspect.SearchApiAspect;
import com.mightwork.searchapi.data.SearchKey;
import com.mightwork.searchapi.test_app.entity.TestEntity;
import com.mightwork.searchapi.test_app.repository.TestEntityRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackageClasses = TestEntityRepository.class)
@ComponentScan(basePackages = {"com.mightwork.searchapi"})
public class BeanConfig implements ApplicationRunner
{
    @Bean
    public SearchApiAspect searchApiAspect()
    {
        return new SearchApiAspect();
    }


    @Autowired
    private TestEntityRepository testEntityRepository;


    @Bean
    public SearchConfigurer<TestEntity> getSearchKeysForItem()
    {
        return new SearchConfigurer<TestEntity>()
        {
            @Override
            public List<SearchKey> getSearchKeys()
            {
                List<SearchKey> searchKeys = new ArrayList();
                searchKeys.add(new SearchKey("age"));
                searchKeys.add(new SearchKey("id"));
                searchKeys.add(new SearchKey("fullName", "name"));
                searchKeys.add(new SearchKey("enrolledDate", "dateCreated"));
                return searchKeys;
            }
        };
    }


    @Override
    public void run(ApplicationArguments applicationArguments)
    {
        testEntityRepository.save(new TestEntity("John Smith", 12, new Date()));
        testEntityRepository.save(new TestEntity("Paul Mc", 10, new Date()));
        testEntityRepository.save(new TestEntity("Alice Kone", 12, new Date()));
        testEntityRepository.save(new TestEntity("Wales Adam", 5, new Date()));
    }
}
