package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Role(null));
    }

    @Test
    public void constructor_invalidRole_throwsIllegalArgumentException() {
        String invalidRole = "";
        assertThrows(IllegalArgumentException.class, () -> new Role(invalidRole));
    }

    @Test
    public void isValidRole() {
        // null role
        assertThrows(NullPointerException.class, () -> Role.isValidRole(null));

        // --- INVALID ROLES ---
        assertFalse(Role.isValidRole("")); // empty string
        assertFalse(Role.isValidRole(" ")); // spaces only
        assertFalse(Role.isValidRole("-")); // hyphen only
        assertFalse(Role.isValidRole("^")); // only non-alphanumeric characters
        assertFalse(Role.isValidRole("!")); // non-alphanumeric character
        assertFalse(Role.isValidRole("@")); // non-alphanumeric character
        assertFalse(Role.isValidRole("HR*")); // contains non-alphanumeric symbols
        assertFalse(Role.isValidRole("HR_Department")); // underscore is not allowed
        assertFalse(Role.isValidRole("123456789012345678901234567890123456789012345678901")); // 51 characters
        assertFalse(Role.isValidRole("Software Engineer Manager Head2")); // 31 characters

        // Edge Case: Leading/Trailing separators
        assertFalse(Role.isValidRole(" Software Engineer")); // starts with a space
        assertFalse(Role.isValidRole("Software Engineer ")); // ends with a space
        assertFalse(Role.isValidRole("-Software Engineer")); // starts with a hyphen
        assertFalse(Role.isValidRole("Software Engineer-")); // ends with a hyphen

        // Edge Case: Consecutive separators
        assertFalse(Role.isValidRole("Software  Engineer")); // consecutive spaces
        assertFalse(Role.isValidRole("Software--Engineer")); // consecutive hyphens
        assertFalse(Role.isValidRole("Software- Engineer")); // consecutive hyphen and space
        assertFalse(Role.isValidRole("Software -Engineer")); // consecutive space and hyphen

        // --- VALID ROLES ---
        assertTrue(Role.isValidRole("a")); // single character
        assertTrue(Role.isValidRole("A")); // single capital character
        assertTrue(Role.isValidRole("1")); // single number
        assertTrue(Role.isValidRole("Software Engineer")); // alphabets with space
        assertTrue(Role.isValidRole("HR")); // alphabets only
        assertTrue(Role.isValidRole("123")); // numbers only
        assertTrue(Role.isValidRole("HR123")); // alphanumeric
        assertTrue(Role.isValidRole("Software-Engineer")); // with hyphen
        assertTrue(Role.isValidRole("Lead-Eng 2nd")); // complex separators with alphanumeric
        assertTrue(Role.isValidRole("123456789012345678901234567890")); // exactly 30 chars
    }

    @Test
    public void equals() {
        Role role = new Role("Valid Role");

        // same values -> returns true
        assertTrue(role.equals(new Role("Valid Role")));

        // case insensitive: different case -> returns true
        assertTrue(role.equals(new Role("valid role")));
        assertTrue(role.equals(new Role("Valid role")));
        assertTrue(role.equals(new Role("VALID ROLE")));

        // same object -> returns true
        assertTrue(role.equals(role));

        // null -> returns false
        assertFalse(role.equals(null));

        // different types -> returns false
        assertFalse(role.equals(5.0f));

        // different values -> returns false
        assertFalse(role.equals(new Role("Other Valid Role")));
    }
}
