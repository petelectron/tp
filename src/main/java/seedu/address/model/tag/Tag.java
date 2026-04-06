package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Tag in the HRManager.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final int MAX_LENGTH = 30;
    public static final String MESSAGE_CONSTRAINTS =
            "Tags should only consist of alphanumeric characters, "
            + "hyphens and spaces, and be between 1 and " + MAX_LENGTH + " characters long.\n"
            + "The tag should not start or end with a space or hyphen, "
            + "and it should not contain consecutive spaces or hyphens.";

    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX =
        "^(?!.*[ -]{2})" // no consecutive spaces or hyphens
        + "[a-zA-Z0-9]" // starts with an alphanumeric character
        + "([a-zA-Z0-9 -]{0," + (MAX_LENGTH - 2) + "}" // alphanumeric, " ", "-" in the middle
        + "[a-zA-Z0-9])?" // ends with an alphanumeric character
        + "$";

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        checkArgument(isValidTagName(tagName), MESSAGE_CONSTRAINTS);
        this.tagName = tagName.toLowerCase();
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equalsIgnoreCase(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.toLowerCase().hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
