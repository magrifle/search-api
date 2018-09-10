package com.github.magrifle.data.searchapi.test_app.controller;

import com.github.magrifle.data.searchapi.data.SearchBuilder;
import com.github.magrifle.data.searchapi.test_app.repository.TestEntityRepository;
import com.github.magrifle.data.searchapi.annotation.SearchApi;
import com.github.magrifle.data.searchapi.test_app.entity.TestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private TestEntityRepository testEntityRepository;


    @SearchApi(entity = TestEntity.class)
    @GetMapping("/search")
    public List<TestEntity> searchItems(SearchBuilder<TestEntity> builder) {
        return testEntityRepository.findAll(builder.build());
    }
}
