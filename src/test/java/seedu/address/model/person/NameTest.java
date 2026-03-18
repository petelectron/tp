package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("^")); // only non-alphanumeric characters
        assertFalse(Name.isValidName("peter*")); // contains non-alphanumeric characters
        assertFalse(Name.isValidName("peter  jack")); // contains consecutive spaces
        assertFalse(Name.isValidName("peter--jack")); // contains consecutive hyphens
        assertFalse(Name.isValidName(" peter jack")); // starts with a space
        assertFalse(Name.isValidName("peter jack ")); // ends with a space
        assertFalse(Name.isValidName("-peter jack")); // starts with a hyphen
        assertFalse(Name.isValidName("peter jack-")); // ends with a hyphen
        assertFalse(Name.isValidName("peter- jack")); // consecutive hyphen and space
        assertFalse(Name.isValidName("peter -jack")); // consecutive space and hyphen
        assertFalse(Name.isValidName("12345")); // numbers only
        assertFalse(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertFalse(Name.isValidName("Alexander-Jonathan-Montgomery-"
            + "Wellington-Smith-Anderson")); // unacceptably long String (prevents attacks)
        assertFalse(Name.isValidName("Alexander Jonathan Montgomery Wellington Smith Anderson"
            + "David Roger Jackson Ray Jr 2nd")); // long names and includes numbers
        assertFalse(Name.isValidName("peter the %nd")); // special characters
        assertFalse(Name.isValidName("peter_pan")); // special characters
        assertFalse(Name.isValidName("peterpan]")); // special characters
        assertFalse(Name.isValidName("a")); // only one character
        assertFalse(Name.isValidName("12345")); // numbers
        assertFalse(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertFalse(Name.isValidName("David Roger Jackson Ray Jr 2nd")); // long names

        // valid name
        assertTrue(Name.isValidName("ct")); // 2 characters only
        assertTrue(Name.isValidName("TAN XIAO MING")); // UPPERCASE characters only
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
}
