package com.backend.database.filtering;

/**
 * Specifies the limits of a query result.
 * @param maxResults Maximum number of results that will be returned by the query.
 * @param resultsStart Index of the first result in the query.
 */
public record Limits(int maxResults, int resultsStart) {

    /**
     * Limits value for a non-limited result.
     */
    public static final Limits NOT_LIMITED = new Limits(Integer.MAX_VALUE, 0);

    /**
     * Create a new limits instance.
     * @param maxResults Maximum number of allowed results.
     */
    public Limits(int maxResults) {
        this(maxResults, 0);
    }
}
