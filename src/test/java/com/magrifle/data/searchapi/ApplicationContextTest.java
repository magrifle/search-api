package com.magrifle.data.searchapi;

import com.magrifle.data.searchapi.test_app.AppConfig;
import com.magrifle.data.searchapi.test_app.BeanConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = {AppConfig.class, BeanConfig.class}
)
public class ApplicationContextTest
{

    @Test
    public void load()
    {

    }
}
