package com.github.magrifle.data.searchapi;

import com.github.magrifle.data.searchapi.data.SearchCriteria;
import com.github.magrifle.data.searchapi.data.SearchKey;
import com.github.magrifle.data.searchapi.exception.SearchDataFormatException;
import com.github.magrifle.data.searchapi.exception.SearchKeyValidationException;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class SearchKeyConfigurerService<T> {
    private final SearchConfigurer<T> searchConfigurer;


    public SearchKeyConfigurerService(final SearchConfigurer<T> searchConfigurer) {
        this.searchConfigurer = searchConfigurer;
    }


    private void validateKey(SearchKey searchKey, SearchCriteria criterion) throws SearchKeyValidationException {

        if (searchKey.getDeniedOperations() != null && Arrays.asList(searchKey.getDeniedOperations()).contains(criterion.getOperation())) {
            throw new SearchKeyValidationException("The search operator [" + criterion.getOperation() + "] is not allowed for the key [" + criterion.getKey() + "]");
        }
    }


    private void transform(SearchCriteria criterion) throws SearchKeyValidationException, SearchDataFormatException {
        if (criterion != null) {
            SearchKey searchKey = this.searchConfigurer.getSearchKeys()
                    .stream()
                    .filter(n -> n.getName().equalsIgnoreCase(criterion.getKey()))
                    .findAny()
                    .orElseThrow(() -> new SearchKeyValidationException(
                            "Unknown search key \"" + criterion.getKey() + "\" was found!"));

            this.validateKey(searchKey, criterion);
            if (this.isDateField(searchKey.getFieldName())) {
                try {
                    criterion.setValue(this.searchConfigurer.getDateKeyFormat().parse(criterion.getValue().toString()));
                } catch (ParseException e) {
                    throw new SearchDataFormatException("Unparseable date value \"" + criterion.getValue()
                            .toString() + "\". The expected format is \"" + this.searchConfigurer.getDateKeyFormat().toLocalizedPattern() + "\"");
                }
            }
            criterion.setKey(searchKey.getFieldName());
        }

    }


    public void prepareSearchData(List<SearchCriteria> criteriaList) throws SearchKeyValidationException, SearchDataFormatException {
        //validate if there is a missing required field
        List<String> searchKeys = criteriaList.stream().map(c -> c.getKey().toLowerCase()).collect(Collectors.toList());

        this.searchConfigurer.getSearchKeys()
                .stream()
                .filter(SearchKey::isRequired)
                .map(s -> s.getName().toLowerCase())
                .collect(Collectors.toList())
                .stream().filter(c -> !searchKeys.contains(c)).forEach(c -> {
            throw new SearchKeyValidationException(
                    "Required search key \"" + c + "\" was not found in the query string");
        });

        for (SearchCriteria searchCriteria : criteriaList) {
            this.transform(searchCriteria);
        }
    }


    private boolean isDateField(String fieldName) {

        return Arrays.stream(this.searchConfigurer.getType().getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .filter(f -> f.getName().equalsIgnoreCase(fieldName))
                .anyMatch(f -> f.getType().isAssignableFrom(Date.class));
    }
}
