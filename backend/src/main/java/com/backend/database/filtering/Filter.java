package com.backend.database.filtering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.backend.database.debug.JavaObjectToSqlLiteralConverterNotSafeForSqlDoNotUseInQuery;

import jakarta.persistence.criteria.Predicate;

/**
 * Opaque object that holds a single filter component.
 * @tparam Entity Database entity this filter applies to.
 * @author JaarmaCo
 * @since 2025-09-23
 * @version 1.0
 */
public class Filter<Entity> {

    private final String field;
    private final Class<Entity> entityClass;
    private final Optional<?> operand;
    private final FilteringMethod method;
    private final Predicate predicate;
    private final Iterable<Filter<Entity>> subFilters;

    /* package-private */ <Op> Filter(Predicate predicate, Class<Entity> entityClass, FilteringMethod method, String field, Op operand) {
        this.predicate = predicate;
        this.method = method;
        this.entityClass = entityClass;
        this.subFilters = Collections.emptyList();
        this.field = field;
        this.operand = Optional.of(operand);
    }

    /* package-private */ Filter(Predicate predicate, Class<Entity> entityClass, FilteringMethod method, Iterable<Filter<Entity>> subFilters) {
        this.predicate = predicate;
        this.method = method;
        this.entityClass = entityClass;
        this.subFilters = subFilters;
        this.field = "(none)";
        this.operand = Optional.empty();
    }

    /* package-private */ Predicate getPredicate() {
        return predicate;
    }

    /**
     * Gets the method used in this filter.
     */
    public FilteringMethod getMethod() {
        return this.method;
    }

    /**
     * Gets the field targeted by this filter (or the string "(none)" for no field).
     */
    public String getField() {
        return field;
    }

    /**
     * Gets an iterable over all sub-filters of this filter.
     */
    public Iterable<Filter<Entity>> getSubFilters() {
        return this.subFilters;
    }

    /**
     * Gets the right hand side operand of this filter (if any).
     * @return Optional that contains the right hand side operand if one was provided.
     */
    public Optional<?> getOperand() {
        return this.operand;
    }

    private static String javaObjectToSqlString(Object object) {
        return JavaObjectToSqlLiteralConverterNotSafeForSqlDoNotUseInQuery.toLiteral(object, 
            JavaObjectToSqlLiteralConverterNotSafeForSqlDoNotUseInQuery.Dialect.POSTGRES);
    }

    /**
     * Full string representation of the specified expression.
     * @warning DO NOT USE IN SQL QUERIES.
     */
    @Override
    public String toString() {
        if (getMethod().isComparison()) {
            return String.format("%s.%s %s %s", 
                entityClass.getName(),
                getField(),
                getMethod().toOperatorString(),
                javaObjectToSqlString(operand.get()));
        } else {
            StringBuilder sb = new StringBuilder();
            List<Filter<Entity>> filters = new ArrayList<>();
            subFilters.forEach(filters::add);

            if (filters.size() == 1) {
                sb.append(getMethod().toOperatorString());
                sb.append(" (");
                sb.append(filters.get(0).toString());
                sb.append(")");
            } else {
                for (int i = 0; i < filters.size(); ++i) {
                    if (i != 0) {
                        sb.append(" ");
                        sb.append(getMethod().toOperatorString());
                        sb.append(" ");
                    }
                    sb.append("(");
                    sb.append(filters.get(i).toString());
                    sb.append(")");
                }
            }
            return sb.toString();
        }
    }
}
