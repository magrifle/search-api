package com.mightwork.searchapi.specification;

import com.mightwork.searchapi.SearchKeyConfigurerService;
import com.mightwork.searchapi.SearchOperation;
import com.mightwork.searchapi.data.SearchCriteria;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationsBuilder<T>
{

    private List<SearchCriteria> params = new ArrayList<>();

    private final SearchKeyConfigurerService<T> searchKeyConfigurerService;


    public SpecificationsBuilder(SearchKeyConfigurerService searchKeyConfigurerService)
    {
        this.searchKeyConfigurerService = searchKeyConfigurerService;
    }


    public SpecificationsBuilder with(
        String key, String operation, Object value, String prefix, String suffix)
    {

        SearchOperation op = SearchOperation.SIMPLE_OPERATION_SET.get(operation.charAt(0));
        if (op != null)
        {
            if (op == SearchOperation.EQUALITY)
            {
                boolean startWithAsterisk = prefix == "*";
                boolean endWithAsterisk = suffix == "*";

                if (startWithAsterisk && endWithAsterisk)
                {
                    op = SearchOperation.CONTAINS;
                }
                else if (startWithAsterisk)
                {
                    op = SearchOperation.ENDS_WITH;
                }
                else if (endWithAsterisk)
                {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            SearchCriteria searchCriteria = new SearchCriteria(key, op, value);
            params.add(searchCriteria);

        }
        return this;
    }


    public Specification<T> build()
    {
        if (params.size() == 0)
        {
            return null;
        }
        this.searchKeyConfigurerService.prepareSearchData(params);
        List<CriteriaSpecification<T>> specs = new ArrayList<>();
        for (SearchCriteria param : params)
        {
            specs.add(new EntitySpecification(param));
        }

        CriteriaSpecification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++)
        {
            result = EntitySpecification.where(result).and(specs.get(i));
        }
        return result;
    }
}