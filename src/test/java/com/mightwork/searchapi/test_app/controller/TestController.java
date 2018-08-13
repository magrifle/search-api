package com.mightwork.searchapi.test_app.controller;

import com.mightwork.searchapi.annotation.SearchApi;
import com.mightwork.searchapi.specification.EntitySpecification;
import com.mightwork.searchapi.test_app.entity.TestEntity;
import com.mightwork.searchapi.test_app.repo.TestEntityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController
{

    @Autowired
    private TestEntityRepository testEntityRepository;


    @SearchApi(entity = TestEntity.class)
    @GetMapping("/search")
    public List<TestEntity> searchItems(EntitySpecification<TestEntity> entitySpecification)
    {
        return testEntityRepository.findAll(entitySpecification);
    }
}
