package com.github.magrifle.data.searchapi;

import com.github.magrifle.data.searchapi.data.SearchCriteria;
import com.github.magrifle.data.searchapi.data.SearchKey;
import com.github.magrifle.data.searchapi.test_app.entity.TestEntity;
import com.github.magrifle.data.searchapi.exception.SearchDataFormatException;
import com.github.magrifle.data.searchapi.exception.SearchKeyValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class SearchKeyConfigurerServiceTest {

    private SearchKeyConfigurerService<TestEntity> objectUnderTest;

    @Mock
    private SearchConfigurer<TestEntity> searchConfigurer;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        objectUnderTest = new SearchKeyConfigurerService<>(searchConfigurer);

        Mockito.when(searchConfigurer.getType()).thenReturn(TestEntity.class);
        Mockito.when(searchConfigurer.getSearchKeys()).thenReturn(getDefaultSearchKeys());
        Mockito.when(searchConfigurer.getDateKeyFormat()).thenCallRealMethod();

    }


    @Test
    public void prepareSearchData_whenValidSearchCriteriaProvided_thenMapSearchCriteria() {
        //GIVEN
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        searchCriteria.add(new SearchCriteria("fullName", SearchOperation.EQUALITY, "John"));
        searchCriteria.add(new SearchCriteria("age", SearchOperation.GREATER_THAN, 12));
        searchCriteria.add(new SearchCriteria("enrolledDate", SearchOperation.LESS_THAN, "2018-08-09"));

        List<SearchKey> searchKeys = searchConfigurer.getSearchKeys();

        //WHEN
        objectUnderTest.prepareSearchData(searchCriteria);

        //THEN
        assertEquals(searchCriteria.size(), 3);
        assertEquals(searchCriteria.get(0).getKey(), searchKeys.get(0).getFieldName());
        assertEquals(searchCriteria.get(1).getKey(), searchKeys.get(1).getFieldName());
        assertEquals(searchCriteria.get(2).getKey(), searchKeys.get(2).getFieldName());
    }


    @Test
    public void prepareSearchData_whenValidDateCriterionProvided_thenMapToDateFormat() {
        //GIVEN
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        searchCriteria.add(new SearchCriteria("enrolledDate", SearchOperation.LESS_THAN, "2018-08-09"));

        List<SearchKey> searchKeys = searchConfigurer.getSearchKeys();

        //WHEN
        objectUnderTest.prepareSearchData(searchCriteria);

        //THEN
        assertEquals(searchCriteria.size(), 1);
        assertEquals(searchCriteria.get(0).getKey(), searchKeys.get(2).getFieldName());
        assertTrue(searchCriteria.get(0).getValue() instanceof Date);
    }


    @Test(expected = SearchDataFormatException.class)
    public void prepareSearchData_whenInValidDateCriterionProvided_thenThrowException() {
        //GIVEN
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        searchCriteria.add(new SearchCriteria("enrolledDate", SearchOperation.LESS_THAN, "2018/08/09"));

        //WHEN / THEN
        objectUnderTest.prepareSearchData(searchCriteria);
    }


    @Test(expected = SearchKeyValidationException.class)
    public void prepareSearchData_whenUnknownKeyCriterionProvided_thenThrowException() {
        //GIVEN
        List<SearchCriteria> searchCriteria = new ArrayList<>();
        searchCriteria.add(new SearchCriteria("city", SearchOperation.EQUALITY, "London"));

        //WHEN / THEN
        objectUnderTest.prepareSearchData(searchCriteria);
    }


    private List<SearchKey> getDefaultSearchKeys() {
        List<SearchKey> searchKeys = new ArrayList<>();
        searchKeys.add(new SearchKey("fullName", "name"));
        searchKeys.add(new SearchKey("age"));
        searchKeys.add(new SearchKey("enrolledDate", "dateCreated"));
        return searchKeys;
    }
}
