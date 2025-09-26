package com.backend.database.filtering;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

/**
 * Class used to send filtered queries to the database.
 * @param <Entity> Database entity to query.
 * @author JaarmaCo
 * @since 2025-09-23
 * @version 1.0
 */
public class FilteredQuery<Entity> {

    private final EntityManager manager;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<Entity> query;
    private final Root<Entity> root;
    private final Class<Entity> entityClass;

    /**
     * Creates a new filtered query.
     * @param manager The entity manager that is currently in use by Jakarta.
     * @param entityClass Literally Entity.class
     */
    public FilteredQuery(EntityManager manager, Class<Entity> entityClass) {
        assert null != manager;
        assert null != entityClass;
        this.manager = manager;
        this.criteriaBuilder = manager.getCriteriaBuilder();
        this.query = criteriaBuilder.createQuery(entityClass);
        this.root = query.from(entityClass);
        this.entityClass = entityClass;
    }

    /**
     * Gets a @see{FilterBuilder} object that can be used to construct the query filter.
     * @return A builder configured to work with this particular query.
     */
    public FilterBuilder<Entity> getFilterBuilder() {
        return new FilterBuilder<>(criteriaBuilder, root, entityClass);
    }

    /**
     * Runs this query with a specified filter.
     * @note This object should be discarded after a call to this method.
     * @param filter Filter to apply to the query.
     * @return A list of the results of the query.
     */
    public List<Entity> runQuery(Filter<Entity> filter) {
        assert null != filter;
        query.select(root).where(filter.getPredicate());
        return manager.createQuery(query).getResultList();
    }
}
