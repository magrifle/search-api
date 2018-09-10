package com.github.magrifle.data.searchapi;

import com.github.magrifle.data.searchapi.exception.SearchKeyValidationException;
import com.github.magrifle.data.searchapi.test_app.AppConfig;
import com.github.magrifle.data.searchapi.test_app.BeanConfig;
import com.github.magrifle.data.searchapi.test_app.entity.TestEntity;
import com.github.magrifle.data.searchapi.test_app.repository.TestEntityRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, BeanConfig.class})
@WebAppConfiguration
public class SearchApiIntegrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestEntityRepository testEntityRepository;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
        testEntityRepository.save(new TestEntity("John Smith", 12, new Date()));
        testEntityRepository.save(new TestEntity("Paul Whales", 10, new Date()));
        testEntityRepository.save(new TestEntity("Alice Conny", 12, new Date()));
        testEntityRepository.save(new TestEntity("Paul Adams", 5, new Date()));
    }

    @Test
    public void searchApi_whenValidQueryProvidedMatchingSingleItem_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=id:1,age:12"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John Smith")))
                .andExpect(jsonPath("$[0].age", is(12)));
    }

    @Test
    public void searchApi_whenValidQueryProvidedMatchingMultipleItems_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=age:12"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Smith")))
                .andExpect(jsonPath("$[1].name", is("Alice Conny")))
                .andExpect(jsonPath("$[0].age", is(12)))
                .andExpect(jsonPath("$[1].age", is(12)));
    }

    @Test
    public void searchApi_whenSearchItemIsLesserThan_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=age<12"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Paul Whales")))
                .andExpect(jsonPath("$[1].name", is("Paul Adams")))
                .andExpect(jsonPath("$[0].age", is(10)))
                .andExpect(jsonPath("$[1].age", is(5)));
    }

    @Test
    public void searchApi_whenSearchItemIsGreaterThan_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=age>5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("John Smith")))
                .andExpect(jsonPath("$[1].name", is("Paul Whales")))
                .andExpect(jsonPath("$[2].name", is("Alice Conny")))
                .andExpect(jsonPath("$[0].age", is(12)))
                .andExpect(jsonPath("$[1].age", is(10)))
                .andExpect(jsonPath("$[2].age", is(12)));
    }

    @Test
    public void searchApi_whenSearchItemLike_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=fullName:*Paul*"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Paul Whales")))
                .andExpect(jsonPath("$[1].name", is("Paul Adams")));
    }

    @Test
    public void searchApi_whenInvalidKeyProvided_thenThrowError() throws Exception {
        // GIVEN / THEN / WHEN
        thrown.expectMessage("Unknown search key \"r\" was found!");

        Exception resolvedException = mvc.perform(get("/search?q=r:1"))
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException();
        assertTrue(resolvedException instanceof SearchKeyValidationException);
    }

}
