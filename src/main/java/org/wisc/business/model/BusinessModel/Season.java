package org.wisc.business.model.BusinessModel;

/**
 * Season
 * Season enum represents the course season.
 */
public enum Season {
    FALL("FALL"), SPRING("SPRING"), SUMMER("SUMMER");

    private final String value;

    public static Season generateSeason(String s) {
        s = s.toLowerCase();
        switch (s) {
            case "fall":
                return FALL;
            case "spring":
                return SPRING;
            case "summer":
                return SUMMER;
        }
        return null;
    }

    Season(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}