package com.api.ewallet.repository.specification;

import org.springframework.data.jpa.domain.Specification;

public class BaseSpecification {

    /**
     * Specification to filter active entities based on 'status' field.
     * Assumes that the entity has a 'status' field where '1' indicates active.
     *
     * @param <T> the type of the entity
     * @return Specification for active entities
     */
    public static <T> Specification<T> isActive() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("status"), "1");
    }
}
