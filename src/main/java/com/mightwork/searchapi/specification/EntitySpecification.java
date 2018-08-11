package com.mightwork.searchapi.specification;

import com.mightwork.searchapi.data.SearchCriteria;
import org.springframework.util.Assert;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Date;

public class EntitySpecification<T> implements CriteriaSpecification<T> {

    private SearchCriteria criteria;

    private CriteriaSpecification<T> spec;


    public EntitySpecification() {
    }


    EntitySpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }


    private EntitySpecification(CriteriaSpecification<T> spec) {
        this.spec = spec;
    }

    static <T> EntitySpecification<T> where(CriteriaSpecification<T> spec) {
        return new EntitySpecification<>(spec);
    }

    @Override
    public Predicate toPredicate(
            Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        SearchCriteria criteria = this.getCriteria();
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

    EntitySpecification<T> and(CriteriaSpecification<T> other) {
        return new EntitySpecification<>(new EntitySpecification.ComposedSpecification<>(this.spec, other, EntitySpecification.CompositionType.AND));
    }


    @Override
    public SearchCriteria getCriteria() {
        if (this.criteria == null) {
            return this.spec != null ? this.spec.getCriteria() : null;
        }
        return this.criteria;
    }


    enum CompositionType {
        AND {
            public Predicate combine(CriteriaBuilder builder, Predicate lhs, Predicate rhs) {
                return builder.and(lhs, rhs);
            }
        };


        CompositionType() {
        }


        abstract Predicate combine(CriteriaBuilder var1, Predicate var2, Predicate var3);
    }

    private static class ComposedSpecification<T> implements CriteriaSpecification<T>, Serializable {
        private static final long serialVersionUID = 1L;
        private final CriteriaSpecification<T> lhs;
        private final CriteriaSpecification<T> rhs;
        private final EntitySpecification.CompositionType compositionType;


        private ComposedSpecification(CriteriaSpecification<T> lhs, CriteriaSpecification<T> rhs, EntitySpecification.CompositionType compositionType) {
            Assert.notNull(compositionType, "CompositionType must not be null!");
            this.lhs = lhs;
            this.rhs = rhs;
            this.compositionType = compositionType;
        }


        @Override
        public SearchCriteria getCriteria() {
            return this.rhs.getCriteria();
        }


        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
            Predicate otherPredicate = this.rhs == null ? null : this.rhs.toPredicate(root, query, builder);
            Predicate thisPredicate = this.lhs == null ? null : this.lhs.toPredicate(root, query, builder);
            return thisPredicate == null ? otherPredicate : (otherPredicate == null ? thisPredicate : this.compositionType.combine(builder, thisPredicate, otherPredicate));
        }
    }
}