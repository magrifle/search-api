package com.github.magrifle.data.searchapi.specification;

import com.github.magrifle.data.searchapi.data.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

public interface CriteriaSpecification<T> extends Specification<T> {
    SearchCriteria getCriteria();
}
