package com.backend.database.filtering;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;


/**
 * Helper class for converting Json to @see{Filter}
 * <p>
 * Note: For now, this class only supports entities with entirely flat columns
 * (I.e no composite key entities).
 * @author JaarmaCo
 * @since 2025-09-23
 * @version 1.0
 */
public abstract class JsonToFilterConverter {

    private JsonToFilterConverter() {}

    private static Object jsonValue(JsonNode json) {
        return switch (json.getNodeType()) {
        case BOOLEAN -> json.asBoolean();
        case NUMBER -> json.numberValue();
        case STRING -> json.asText();
        default -> throw new IllegalArgumentException("Expected a scalar type."); 
        };
    }

    private static <Entity> Filter<Entity> comparisonFilterFromJson(FilterBuilder<Entity> builder, JsonNode json, FilteringMethod method) {

        if (!json.has("field"))
            throw new IllegalArgumentException("Missing expected field \"field\"");

        if (!json.has("value"))
            throw new IllegalArgumentException("Missing expected field \"value\"");

        String field = json.get("field").asText();
        JsonNode rhs = json.get("value");

        return switch (method) {
        case LESS
            -> builder.lessThan(field, rhs.numberValue());
        case GREATER
            -> builder.greaterThan(field, rhs.numberValue());
        case EQUALS
            -> builder.equalTo(field, jsonValue(rhs));
        case LIKE
            -> builder.like(field, rhs.asText());
        default
            -> throw new IllegalArgumentException("Invalid method.");
        };   
    }

    private static <Entity> Filter<Entity> booleanFilterFromJson(FilterBuilder<Entity> builder, JsonNode json, FilteringMethod method) {
        if (!json.has("arguments"))
            throw new IllegalArgumentException("Missing expected field \"arguments\"");

        List<Filter<Entity>> subFilters = new ArrayList<>();
        for (JsonNode subFilter : json.get("arguments")) {
            subFilters.add(filterFromJson(builder, subFilter));
        }
        return switch (method) {
        case NOT
            -> builder.not(subFilters.get(0));
        case OR
            -> builder.or(subFilters);
        case AND
            -> builder.and(subFilters);
        default
            -> throw new IllegalArgumentException("Invalid method.");
        };
    }

    /**
     * Creates a filter as described by the provided Json object.
     * @param <Entity> Type of entity to create the filter for.
     * @param builder The builder used to build the filter.
     * @param json The json object describing the filter.
     * @return A filter that corresponds to the describing Json.
     */
    public static <Entity> Filter<Entity> filterFromJson(FilterBuilder<Entity> builder, JsonNode json) {
        assert null != builder;
        assert null != json;

        if (json.isArray()) {
            List<Filter<Entity>> filters = new ArrayList<>();
            for (JsonNode subnode : json) {
                filters.add(filterFromJson(builder, subnode));
            }
            return builder.and(filters);
        }

        if (!json.has("filter"))
            throw new IllegalArgumentException("Missing required property \"filter\".");

        FilteringMethod method = FilteringMethod.parse(json.get("filter").asText());
        return switch (method) {
            case NOT, OR, AND -> booleanFilterFromJson(builder, json, method);
            default -> comparisonFilterFromJson(builder, json, method);
        };
    }

    /**
     * Translate ordering, limits and filters from the specified json and execute the query.
     * @param <Entity> Entity type to query.
     * @param query Query to construct.
     * @param json The json containing the properties of the json to run.
     * @return The list of results.
     */
    public static <Entity> List<Entity> runQueryFromJson(FilteredQuery<Entity> query, JsonNode json) {
        assert null != query;
        assert null != json;

        Ordering order;
        Limits limits;
        order = json.has("sorting") ? Ordering.fromJson(json.get("sorting")) : Ordering.NONE;
        
        int start = 0;
        int maxResults = Integer.MAX_VALUE;

        if (json.has("first"))
            start = json.get("first").asInt();
        if (json.has("max_count"))
            maxResults = json.get("max_count").asInt();

        limits = new Limits(maxResults, start);
        
        if (json.has("filters")) {
            return query.runQuery(filterFromJson(query.getFilterBuilder(), json.get("filters")), order, limits);
        }
        return query.runQuery(order, limits);
    }
}
