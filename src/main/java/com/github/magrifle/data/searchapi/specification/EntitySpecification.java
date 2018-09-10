package com.github.magrifle.data.searchapi.specification;

import com.github.magrifle.data.searchapi.data.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Date;

public class EntitySpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    public EntitySpecification() {
    }


    EntitySpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(
            Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria == null) {
            return null;
        }
        switch (criteria.getOperation()) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                if (root.get(criteria.getKey()).getJavaType() == Date.class) {
                    return builder.greaterThan(root.get(criteria.getKey()), ((Date) criteria.getValue()));
                } else {
                    return builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LESS_THAN:
                if (root.get(criteria.getKey()).getJavaType() == Date.class) {
                    return builder.lessThan(root.get(criteria.getKey()), ((Date) criteria.getValue()));
                } else {
                    return builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
                }
            case LIKE:
                return builder.like(root.get(
                        criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(root.get(
                        criteria.getKey()), "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }
}