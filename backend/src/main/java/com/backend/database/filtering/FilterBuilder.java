package com.backend.database.filtering;

import java.util.List;
import java.util.stream.StreamSupport;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Builds @see{Filter} objects that can later be applied in a @see{FilteredQuery}
 * @tparam Entity Database entity being filtered.
 * @author JaarmaCo
 * @since 2025-09-23
 * @version 1.0
 */
public class FilterBuilder<Entity> {

    private final Class<Entity> entityClass;
    private final CriteriaBuilder cb;
    private final Root<Entity> root;

    /* package-private */ FilterBuilder(CriteriaBuilder builder, Root<Entity> root, Class<Entity> entityClass) {
        assert null != builder;
        assert null != root;
        assert null != entityClass;
        this.cb = builder;
        this.root = root;
        this.entityClass = entityClass;
    }

    /**
     * Create a less than filter.
     * @tparam Value Numeric type.
     * @param field Field to compare to.
     * @param value Value to assign as the right hand operand.
     * @return A filter that, when applied to a query, filters: Entity.field < value
     */
    public <Value extends Number> Filter<Entity> lessThan(String field, Value value) {
        return new Filter<>(cb.lt(root.get(field), value), entityClass, FilteringMethod.LESS, field, value);
    }

    /**
     * Create a greater than filter.
     * @tparam Value Numeric type.
     * @param field Field to compare to.
     * @param value Value to assign as the right hand operand.
     * @return A filter that, when applied to a query, filters: Entity.field > value
     */
    public <Value extends Number> Filter<Entity> greaterThan(String field, Value value) {
        return new Filter<>(cb.gt(root.get(field), value), entityClass, FilteringMethod.GREATER, field, value);
    }

    /**
     * Create an equal-to filter.
     * @tparam Value Numeric type.
     * @param field Field to compare to.
     * @param value Value to assign as the right hand operand.
     * @return A filter that, when applied to a query, filters: Entity.field = value 
     */
    public <Value extends Number> Filter<Entity> equalTo(String field, Value value) {
        return new Filter<>(cb.equal(root.get(field), value), entityClass, FilteringMethod.EQUALS, field, value);
    }

    /**
     * Create a string pattern filter.
     * @param field Field to compare to.
     * @param value Value to assign as the right hand operand.
     * @return A filter that, when applied to a query, filters: Entity.field LIKE value
     */
    public Filter<Entity> like(String field, String value) {
        return new Filter<>(cb.like(root.get(field), value), entityClass, FilteringMethod.LIKE, field, value);
    }

    private Predicate[] predicates(Iterable<Filter<Entity>> filters) {
        return StreamSupport.stream(filters.spliterator(), false)
            .map((Filter<Entity> f) -> f.getPredicate())
            .toArray(Predicate[]::new);
    }

    /**
     * Create a filter that acts as a boolean OR of other filters.
     * @param filters Filters to OR together.
     * @return A filter that corresponds to: filters OR ...
     */
    public Filter<Entity> or(Iterable<Filter<Entity>> filters) {
        return new Filter<>(cb.or(predicates(filters)), entityClass, FilteringMethod.OR, filters);
    }

    /**
     * Create a filter that acts as a boolean AND of other filters. 
     * @param filters Filters to AND together.
     * @return A filter that corresponds to: filters AND ...
     */
    public Filter<Entity> and(Iterable<Filter<Entity>> filters) {
        return new Filter<>(cb.and(predicates(filters)), entityClass, FilteringMethod.AND, filters);
    }

    /**
     * Create a filter that acts as a boolean NOT of another filter.
     * @param filter Filter to invert.
     * @return A filter that inverts the result of the provided filter.
     */
    public Filter<Entity> not(Filter<Entity> filter) {
        return new Filter<>(cb.not(filter.getPredicate()), entityClass, FilteringMethod.NOT, List.of(filter));
    }
}
