package com.mightwork.searchapi.specification;

import com.mightwork.searchapi.data.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

public interface CriteriaSpecification<T> extends Specification<T>
{
    SearchCriteria getCriteria();
}
