package com.magrifle.data.searchapi;

import com.magrifle.data.searchapi.exception.SearchKeyValidationException;
import com.magrifle.data.searchapi.test_app.AppConfig;
import com.magrifle.data.searchapi.test_app.BeanConfig;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, BeanConfig.class})
@WebAppConfiguration
public class ApplicationContextTest {

    @Test
    public void load() {

    }

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
    public void greetingShouldReturnDefaultMessage() throws Exception {
        String contentAsString = mvc.perform(get("/search?q=id:1"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    public void searchApi_whenInvalidKeyProvided_thenThrowError() throws Exception {
        thrown.expectMessage("Unknown search key \"r\" was found!");

            Exception resolvedException = mvc.perform(get("/search?q=r:1"))
                    .andExpect(status().isInternalServerError())
                    .andReturn().getResolvedException();
            assertTrue(resolvedException instanceof SearchKeyValidationException);
    }

}
