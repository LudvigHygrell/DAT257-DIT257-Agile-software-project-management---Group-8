package com.backend.database.filtering;

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
