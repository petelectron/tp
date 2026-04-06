package seedu.address.model;

import java.util.Locale;
import java.util.Optional;

/**
 * Represents the available statistics dashboard modes.
 */
public enum StatisticsMode {

    TAG("tag", "t"),
    DEPARTMENT("department", "d"),
    ROLE("role", "r");

    private final String fullName;
    private final String shorthand;

    StatisticsMode(String fullName, String shorthand) {
        this.fullName = fullName;
        this.shorthand = shorthand;
    }

    /**
     * Parses user input into a {@code StatisticsMode}.
     */
    public static Optional<StatisticsMode> fromUserInput(String rawInput) {
        if (rawInput == null) {
            return Optional.empty();
        }

        String normalized = rawInput.trim().toLowerCase(Locale.ROOT);
        if ("dept".equals(normalized)) {
            return Optional.of(DEPARTMENT);
        }

        for (StatisticsMode mode : values()) {
            if (mode.fullName.equals(normalized) || mode.shorthand.equals(normalized)) {
                return Optional.of(mode);
            }
        }
        return Optional.empty();
    }

    public String getFullName() {
        return fullName;
    }
}

