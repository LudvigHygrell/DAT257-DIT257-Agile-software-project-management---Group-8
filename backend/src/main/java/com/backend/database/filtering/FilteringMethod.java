package com.backend.database.filtering;

import java.util.Optional;

/**
 * Enumerates different filtering methods that may be applied.
 * @author JaarmaCo
 * @since 2025-09-23
 * @version 1.0
 */
public enum FilteringMethod {

    /**
     * Filter using comparison operator less '<'
     */
    LESS(0),

    /**
     * Filter using comparison operator greater '>'
     */
    GREATER(1),

    /**
     * Filter using comparison operator equal '=='
     */
    EQUALS(2),

    /**
     * Filter using the SQL LIKE operator.
     */
    LIKE(3),

    /**
     * Invert result of sub-filters.
     */
    NOT(99),

    /**
     * Inclusive-join results from sub-filters.
     */
    OR(100),

    /**
     * Exclusive join results from sub-filters.
     */
    AND(101);

    private final int value;
    
    private FilteringMethod(int value) {
        this.value = value;
    }

    /**
     * Gets the integral representation of the filter. 
     */
    public int toInt() {
        return value;
    }

    /**
     * True if the filtering method is a value comparison.  
     */
    public boolean isComparison() {
        return value < NOT.value;
    }

    /**
     * True if the filtering method is a boolean operation.
     */
    public boolean isBooleanExpression() {
        return value >= NOT.value;
    }

    /**
     * Gets the string representation of the filter.
     */
    @Override
    public String toString() {
        return switch (value) {
            case 0 -> "less";
            case 1 -> "greater";
            case 2 -> "equals";
            case 3 -> "like";
            case 99 -> "not";
            case 100 -> "or";
            default -> "and";
        };
    }

    /**
     * Gets the SQL operator string equivalent of this filtering method.
     */
    public String toOperatorString() {
        return switch (value) {
            case 0 -> "<";
            case 1 -> ">";
            case 2 -> "=";
            case 3 -> "LIKE";
            case 99 -> "NOT";
            case 100 -> "OR";
            default -> "AND";
        };
    }

    /**
     * Parses a filtering method from the specified text.
     * @param text Text to parse.
     * @return The filtering method refered to by the given text, or Optional.empty().
     */
    public static Optional<FilteringMethod> tryParse(String text) {
        return switch (text) {
            case "less" -> Optional.of(LESS);
            case "greater" -> Optional.of(GREATER);
            case "equals" -> Optional.of(EQUALS);
            case "like" -> Optional.of(LIKE);
            case "not" -> Optional.of(NOT);
            case "or" -> Optional.of(OR);
            case "and" -> Optional.of(AND);
            default -> Optional.empty();
        };
    }

    /**
     * Parses a filtering method from the specified text.
     * @param text Text to parse.
     * @return The filtering method refered to by the given text.
     * @throws IllegalArgumentException Thrown text did not equal any of the enum string representations.
     */
    public static FilteringMethod parse(String text) throws IllegalArgumentException {
        Optional<FilteringMethod> value = tryParse(text);
        return value.orElseThrow(() -> new IllegalArgumentException("Failed to parse enum."));
    }
}
