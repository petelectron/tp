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

        // --- INVALID NAMES ---
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName("-")); // hyphen only
        assertFalse(Name.isValidName("^")); // only non-alphanumeric characters
        assertFalse(Name.isValidName("peter*")); // contains non-alphanumeric symbols
        assertFalse(Name.isValidName("peter_pan")); // underscore is not allowed
        assertFalse(Name.isValidName("peter the %nd")); // special characters
        assertFalse(Name.isValidName("peter_pan")); // special characters
        assertFalse(Name.isValidName("peterpan]")); // special characters

        // Edge Case: Leading/Trailing separators
        assertFalse(Name.isValidName(" peter jack")); // starts with a space
        assertFalse(Name.isValidName("peter jack ")); // ends with a space
        assertFalse(Name.isValidName("-peter jack")); // starts with a hyphen
        assertFalse(Name.isValidName("peter jack-")); // ends with a hyphen

        // Edge Case: Consecutive separators
        assertFalse(Name.isValidName("peter  jack")); // consecutive spaces
        assertFalse(Name.isValidName("peter--jack")); // consecutive hyphens
        assertFalse(Name.isValidName("peter- jack")); // consecutive hyphen and space
        assertFalse(Name.isValidName("peter -jack")); // consecutive space and hyphen

        // Length (Max 50)
        assertFalse(Name.isValidName("Alexander-Jonathan-Montgomery-"
            + "Wellington-Smith-Anderson")); // 55 characters
        assertFalse(Name.isValidName("Alexander-Jonathan-Montgomery-"
            + "Wellington-Smith-Ande")); // 51 characters

        // --- VALID NAMES ---
        assertTrue(Name.isValidName("a")); // single character
        assertTrue(Name.isValidName("A")); // single capital character
        assertTrue(Name.isValidName("1")); // single number
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("TAN XIAO MING")); // all capital letters
        assertTrue(Name.isValidName("Mary-Jane")); // with hyphen
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr 2nd")); // long alphanumeric name
        assertTrue(Name.isValidName("A-1 B-2 C-3")); // complex separators with alphanumeric
        assertTrue(Name.isValidName("01234567890123456789012345678901234567890123456789")); // exactly 50 chars
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // case insensitive: different case -> returns true
        assertTrue(name.equals(new Name("valid name")));
        assertTrue(name.equals(new Name("Valid name")));
        assertTrue(name.equals(new Name("valid Name")));
        assertTrue(name.equals(new Name("VALID NAME")));
        assertTrue(name.equals(new Name("VaLiD NaMe")));

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
