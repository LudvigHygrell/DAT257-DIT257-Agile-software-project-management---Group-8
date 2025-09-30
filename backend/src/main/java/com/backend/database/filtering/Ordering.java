package com.backend.database.filtering;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Ordering applied to filtered queries.
 * 
 * @param field Field refered to in the ordering.
 * @param direction Value < 0 means descending order, > 0 ascending and 0 no ordering.
 */
public record Ordering(String field, int direction) {

    /**
     * Value passed when no ordering is to be applied.
     */
    public static final Ordering NONE = new Ordering("", 0);

    /**
     * Convert to Ordering from a json object.
     */
    public static Ordering fromJson(JsonNode node) {
        String field = "";
        int dir = 0;

        if (node.has("field"))
            field = node.get("field").asText();

        if (node.has("ordering"))
            dir = switch (node.get("ordering").asText()) {
                case "ascending" -> 1;
                case "descending" -> -1;
                default -> 0;
            };

        if (dir != 0 && field.isEmpty())
            throw new IllegalArgumentException("Field not specified in asc/desc ordering.");

        return new Ordering(field, dir); 
    }

    /**
     * Creates an ascending ordering.
     * @param field Field to order by.  
     */
    public static Ordering ascending(String field) {
        return new Ordering(field, 1);
    }

    /**
     * Creates an descending ordering.
     * @param field Field to order by.  
     */
    public static Ordering descending(String field) {
        return new Ordering(field, -1);
    }

    /**
     * True if the ordering in in ascending order.
     */
    public boolean isAscending() {
        return direction > 0;
    }

    /**
     * True if the ordereing is in descending order. 
     */
    public boolean isDescending() {
        return direction < 0;
    }

    /**
     * True if no ordering is applied.
     */
    public boolean isUnordered() {
        return direction == 0;
    }

    /**
     * True if ordering is either ascending or descending.
     */
    public boolean isOrdered() {
        return direction != 0;
    }
}
