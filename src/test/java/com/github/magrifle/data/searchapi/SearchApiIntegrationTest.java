package com.github.magrifle.data.searchapi;

import com.github.magrifle.data.searchapi.exception.SearchKeyValidationException;
import com.github.magrifle.data.searchapi.test_app.AppConfig;
import com.github.magrifle.data.searchapi.test_app.BeanConfig;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, BeanConfig.class})
@WebAppConfiguration
@SqlGroup({
        @Sql(
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
                scripts = "classpath:data/beforeTestRun.sql",
                config = @SqlConfig(errorMode = SqlConfig.ErrorMode.CONTINUE_ON_ERROR)),
        @Sql(
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
                scripts = "classpath:data/afterTestRun.sql",
                config = @SqlConfig(errorMode = SqlConfig.ErrorMode.CONTINUE_ON_ERROR)
        )
})
public class SearchApiIntegrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void searchApi_whenValidQueryProvidedMatchingSingleItem_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=id:1,age:12,childName:Tobi,manyName:*gr*"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John Smith")))
                .andExpect(jsonPath("$[0].age", is(12)))
                .andExpect(jsonPath("$[0].childEntity.name", is("Tobi")))
                .andExpect(jsonPath("$[0].manyEntities", hasSize(2)))
                .andExpect(jsonPath("$[0].manyEntities[0].name", is("Margret")))
                .andExpect(jsonPath("$[0].manyEntities[1].name", is("Ingrid")));
    }

    @Test
    public void searchApi_whenValidQueryProvidedMatchingMultipleItems_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=age:12"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.age==12)]", hasSize(2)));
    }

    @Test
    public void searchApi_whenSearchItemIsNotEqual_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=age!12"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.age == 12)]").doesNotExist());
    }

    @Test
    public void searchApi_whenSearchItemIsLesserThan_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=age<12"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.age < 12)]", hasSize(2)));
    }

    @Test
    public void searchApi_whenSearchItemIsGreaterThan_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=age>5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[?(@.age > 5)]", hasSize(3)));
    }

    @Test
    public void searchApi_whenSearchItemLike_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=fullName:*Paul*"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[?(@.name == 'Paul Whales')]").exists())
                .andExpect(jsonPath("$[?(@.name == 'Paul Adams')]").exists());
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

    @Test
    public void searchApi_withEnumField_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=role:USER"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("John Smith")))
            .andExpect(jsonPath("$[0].age", is(12)))
            .andExpect(jsonPath("$[0].childEntity.name", is("Tobi")))
            .andExpect(jsonPath("$[0].manyEntities", hasSize(2)))
            .andExpect(jsonPath("$[0].manyEntities[0].name", is("Margret")))
            .andExpect(jsonPath("$[0].manyEntities[1].name", is("Ingrid")));
    }

    @Test
    public void searchApi_withBooleanFieldSetToTrue_thenReturnData() throws Exception {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=human:true"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("John Smith")))
            .andExpect(jsonPath("$[0].age", is(12)))
            .andExpect(jsonPath("$[0].childEntity.name", is("Tobi")))
            .andExpect(jsonPath("$[0].manyEntities", hasSize(2)))
            .andExpect(jsonPath("$[0].manyEntities[0].name", is("Margret")))
            .andExpect(jsonPath("$[0].manyEntities[1].name", is("Ingrid")));
    }

    @Test
    public void searchApi_withBooleanFieldSetToFalse_thenReturnData() throws Exception
    {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=human:false"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("Paul Whales")))
            .andExpect(jsonPath("$[0].age", is(10)));
    }


    @Test
    public void searchApi_withValuesIsFound_thenReturnData() throws Exception
    {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=age>8,id:[1_2]"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*].name", containsInAnyOrder("John Smith", "Paul Whales")))
            .andExpect(jsonPath("$[*].age", containsInAnyOrder(10, 12)));
    }

    @Test
    public void searchApi_withValuesIsNotFound_thenReturnEmpty() throws Exception
    {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=id:[10]"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void searchApi_whenSearchValueContainsSpaces_thenReturnData() throws Exception
    {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=fullName:Alice%20Conny%20Victoria"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Alice Conny Victoria")))
                .andExpect(jsonPath("$[0].age", is(12)));
    }

    @Test
    public void searchApi_whenNoSearchParameter_thenReturnAllData() throws Exception
    {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void searchApi_whenSearchValueContainsAtSymbolAndDot_thenReturnData() throws Exception
    {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=email:alice%2Econny%40email.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Alice Conny Victoria")))
                .andExpect(jsonPath("$[0].age", is(12)));
    }

    @Test
    public void searchApi_withDiscriminatorParentFieldValuesFound_thenReturnData() throws Exception
    {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=numberOfWheels:4"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("John Smith")));
    }

    @Test
    public void searchApi_withDiscriminatorChildFieldValuesFound_thenReturnData() throws Exception
    {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=numberOfDoors:4"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("John Smith")));
    }

    @Test
    public void searchApi_withDiscriminatorChildFieldAndParentValuesFound_thenReturnData() throws Exception
    {
        // GIVEN / THEN / WHEN
        mvc.perform(get("/search?q=numberOfDoors:4,age:12"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(1)))
            .andExpect(jsonPath("$[0].name", is("John Smith")));
    }
}
