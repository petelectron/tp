package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's role in HRmanager.
 * Guarantees: immutable; is valid as declared in {@link #isValidRole(String)}
 */
public class Role {

    public static final int MAX_LENGTH = 30;
    public static final String MESSAGE_CONSTRAINTS =
            "Roles should only consist of alphanumeric characters, "
            + "hyphens and spaces, and be between 1 and " + MAX_LENGTH + " characters long.\n"
            + "The role should not start or end with a space or hyphen, "
            + "and it should not contain consecutive spaces or hyphens.";

    /*
     * The first character of the role must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX =
        "^(?!.*[ -]{2})" // no consecutive spaces or hyphens
        + "[a-zA-Z0-9]" // starts with an alphanumeric character
        + "([a-zA-Z0-9 -]{0," + (MAX_LENGTH - 2) + "}" // alphanumeric, " ", "-" in the middle
        + "[a-zA-Z0-9])?" // ends with an alphanumeric character
        + "$";

    public final String value;

    /**
     * Constructs an {@code Role}.
     *
     * @param role A valid role.
     */
    public Role(String role) {
        requireNonNull(role);
        checkArgument(isValidRole(role), MESSAGE_CONSTRAINTS);
        value = role.toLowerCase();
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidRole(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Role)) {
            return false;
        }

        Role otherRole = (Role) other;
        return value.equalsIgnoreCase(otherRole.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase().hashCode();
    }

}
