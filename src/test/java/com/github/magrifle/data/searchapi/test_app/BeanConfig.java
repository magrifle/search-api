package com.github.magrifle.data.searchapi.test_app;

import com.github.magrifle.data.searchapi.SearchConfigurer;
import com.github.magrifle.data.searchapi.aspect.DataSearchApi;
import com.github.magrifle.data.searchapi.data.SearchKey;
import com.github.magrifle.data.searchapi.test_app.entity.CarEntity;
import com.github.magrifle.data.searchapi.test_app.entity.Role;
import com.github.magrifle.data.searchapi.test_app.entity.TestEntity;
import com.github.magrifle.data.searchapi.test_app.repository.TestEntityRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackageClasses = TestEntityRepository.class)
@ComponentScan(basePackages = {"com.github.magrifle.data.searchapi"})
public class BeanConfig {

    @Bean
    public DataSearchApi searchApiAspect() {
        return new DataSearchApi();
    }

    @Bean
    public SearchConfigurer<TestEntity> getSearchKeysForItem() {
        return new SearchConfigurer<TestEntity>() {
            @Override
            public List<SearchKey> getSearchKeys()
            {
                List<SearchKey> searchKeys = new ArrayList<>();
                searchKeys.add(new SearchKey("age"));
                searchKeys.add(new SearchKey("id"));
                searchKeys.add(new SearchKey("email"));
                searchKeys.add(new SearchKey("fullName", "name"));
                searchKeys.add(new SearchKey("nationality", "nationality"));
                searchKeys.add(new SearchKey("enrolledDate", "dateCreated"));
                searchKeys.add(new SearchKey("childName", "childEntity.name"));
                searchKeys.add(new SearchKey("manyName", "manyEntities.name"));
                searchKeys.add(new SearchKey("childGender", "childEntity.gender"));
                searchKeys.add(new SearchKey("childOccupation", "childEntity.occupation"));
                searchKeys.add(new SearchKey("role", Role::valueOf));
                searchKeys.add(new SearchKey("human", Boolean::new));
                searchKeys.add(new SearchKey("numberOfWheels", "vehicleEntity.numberOfWheels"));
                SearchKey numberOfDoors = new SearchKey("numberOfDoors", "vehicleEntity.numberOfDoors");
                numberOfDoors.setType(CarEntity.class);
                searchKeys.add(numberOfDoors);
                return searchKeys;
            }
        };
    }
}
