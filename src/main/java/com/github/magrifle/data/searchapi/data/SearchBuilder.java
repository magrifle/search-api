package com.github.magrifle.data.searchapi.data;

import org.springframework.data.jpa.domain.Specification;

public class SearchBuilder<T> {

    private Specification<T> specification;

    SearchBuilder() {
    }

    public Specification<T> build() {
        return specification;
    }

    public SearchBuilder(Specification<T> specification) {
        this.specification = specification;
    }

}
