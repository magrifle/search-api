package com.github.magrifle.data.searchapi.specification;

import com.github.magrifle.data.searchapi.SearchOperation;
import com.github.magrifle.data.searchapi.data.SearchCriteria;
import java.util.Arrays;
import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class EntitySpecification<T> implements Specification<T> {

    private SearchCriteria criteria;

    public EntitySpecification() {
    }


    EntitySpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(
        Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder)
    {
        if (criteria == null)
        {
            return null;
        }

        Path path;
        String[] joins = criteria.getKey().split("\\.");
        if (joins.length > 1)
        {
            Join join = root.join(joins[0]);
            path = criteria.getType() != null ? builder.treat(join, criteria.getType()).get(joins[1]) : join.get(joins[1]);
        }
        else
        {
            path = root.get(criteria.getKey());
            query.distinct(true);
        }

        return getCriteria(builder, path);
    }


    private Predicate getCriteria(CriteriaBuilder builder, Path path)
    {
        switch (criteria.getOperation())
        {
            case EQUALITY:
            case NEGATION:
                Predicate predicate = isCaseInsensitive(criteria, path) ? builder.equal(builder.lower(path), criteria.getValue().toString().toLowerCase()) :
                        builder.equal(path, criteria.getValue());
                return criteria.getOperation() == SearchOperation.NEGATION ? predicate.not() : predicate;
            case GREATER_THAN:
                return path.getJavaType() == Date.class ? builder.greaterThan(path, ((Date) criteria.getValue())) : builder.greaterThan(path, criteria.getValue().toString());
            case LESS_THAN:
                return path.getJavaType() == Date.class ? builder.lessThan(path, ((Date) criteria.getValue())) : builder.lessThan(path, criteria.getValue().toString());
            case LIKE:
                return isCaseInsensitive(criteria, path) ? builder.like(builder.lower(path), criteria.getValue().toString().toLowerCase()) :
                        builder.like(path, criteria.getValue().toString());
            case STARTS_WITH:
                return isCaseInsensitive(criteria, path) ? builder.like(builder.lower(path), criteria.getValue().toString().toLowerCase() + "%") :
                        builder.like(path, criteria.getValue() + "%");
            case ENDS_WITH:
                return isCaseInsensitive(criteria, path) ? builder.like(builder.lower(path), "%" + criteria.getValue().toString().toLowerCase()) :
                        builder.like(path, "%" + criteria.getValue());
            case CONTAINS:
                return isCaseInsensitive(criteria, path) ? builder.like(builder.lower(path), "%" + criteria.getValue().toString().toLowerCase() + "%") :
                        builder.like(path, "%" + criteria.getValue() + "%");
            case IN:
                CriteriaBuilder.In inClause = isCaseInsensitive(criteria, path) ? builder.in(builder.lower(path)) : builder.in(path);
                Arrays.asList(criteria.getValue().toString().split("_"))
                    .forEach(v -> {
                        if (path.getJavaType().isAssignableFrom(Long.class)) {
                            inClause.value(Long.valueOf(v));
                        }else if(isCaseInsensitive(criteria, path)){
                            inClause.value(v.toLowerCase());
                        }else{
                            inClause.value(v);
                        }
                    });
                return inClause;
            default:
                return null;
        }
    }

    private boolean isCaseInsensitive(SearchCriteria criteria, Path path){
        return !criteria.isCaseSensitive() && path.getJavaType() == String.class;
    }

}
