package com.github.magrifle.data.searchapi.specification;

import com.github.magrifle.data.searchapi.SearchOperation;
import com.github.magrifle.data.searchapi.data.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
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

        Path path;
        String[] joins = criteria.getKey().split("\\.");
        if (joins.length > 1) {
            Join join = root.join(joins[0]);
            path = join.get(joins[1]);

        } else {
            path = root.get(criteria.getKey());
            query.distinct(true);
        }


        switch (criteria.getOperation()) {
            case EQUALITY:
            case NEGATION:
                Predicate predicate = builder.equal(path, criteria.getValue());
                return criteria.getOperation() == SearchOperation.NEGATION ? predicate.not() : predicate;
            case GREATER_THAN:
                return path.getJavaType() == Date.class ? builder.greaterThan(path, ((Date) criteria.getValue())) : builder.greaterThan(path, criteria.getValue().toString());
            case LESS_THAN:
                return path.getJavaType() == Date.class ? builder.lessThan(path, ((Date) criteria.getValue())) : builder.lessThan(path, criteria.getValue().toString());
            case LIKE:
                return builder.like(path, criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(path, criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(path, "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(path, "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }

}
