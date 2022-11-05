package com.github.magrifle.data.searchapi.specification;

import com.github.magrifle.data.searchapi.SearchKeyConfigurerService;
import com.github.magrifle.data.searchapi.SearchOperation;
import com.github.magrifle.data.searchapi.data.SearchCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationsBuilder<T> {

    private final SearchKeyConfigurerService<T> searchKeyConfigurerService;
    private List<SearchCriteria> params = new ArrayList<>();


    public SpecificationsBuilder(SearchKeyConfigurerService<T> searchKeyConfigurerService) {
        this.searchKeyConfigurerService = searchKeyConfigurerService;
    }


    public SpecificationsBuilder with(
            String key, String operation, Object value, String prefix, String suffix, boolean caseSensitive) {

        SearchOperation op = SearchOperation.SIMPLE_OPERATION_SET.get(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix.equals("*");
                boolean endWithAsterisk = suffix.equals("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk)
                {
                    op = SearchOperation.ENDS_WITH;
                }
                else if (endWithAsterisk)
                {
                    op = SearchOperation.STARTS_WITH;
                }
                else if (prefix.equals("[") && suffix.equals("]"))
                {
                    op = SearchOperation.IN;
                }
            }
            SearchCriteria searchCriteria = new SearchCriteria(key, op, value);
            searchCriteria.setCaseSensitive(caseSensitive);
            params.add(searchCriteria);

        }
        return this;
    }


    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }
        this.searchKeyConfigurerService.prepareSearchData(params);
        List<Specification<T>> specs = new ArrayList<>();
        for (SearchCriteria param : params) {
            specs.add(new EntitySpecification<>(param));
        }

        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Objects.requireNonNull(Specification.where(result)).and(specs.get(i));
        }
        return result;
    }
}
