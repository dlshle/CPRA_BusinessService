package org.wisc.business.model.BusinessModel;

/**
 * Season
 * Season enum represents the course season.
 */
public enum Season {
    FALL("FALL"), SPRING("SPRING"), SUMMER("SUMMER");

    private final String value;

    Season(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}