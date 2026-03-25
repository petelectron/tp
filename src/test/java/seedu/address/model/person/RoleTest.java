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
        

        // invalid roles
        assertFalse(Role.isValidRole("")); // empty string
        assertFalse(Role.isValidRole(" ")); // spaces only
        assertFalse(Role.isValidRole("^")); // non-alphanumeric character
        assertFalse(Role.isValidRole("!")); // non-alphanumeric character
        assertFalse(Role.isValidRole("@")); // non-alphanumeric character
        assertFalse(Role.isValidRole("-")); // one character
        assertFalse(Role.isValidRole("HR*")); // contains  non-alphanumeric character
        assertFalse(Role.isValidRole("HR_Department")); // contains underscore non-alphanumeric character
        assertFalse(Role.isValidRole("HR-Department")); // contains hyphen non-alphanumeric character
        assertFalse(Role.isValidRole("123456789012345678901234567890123456789012345678901")); // 51 characters
        assertFalse(Role.isValidRole("Department 3 4th division head of marketing and security")); // 56 characters 


        // valid roles
        assertTrue(Role.isValidRole("Software Engineer"));
        assertTrue(Role.isValidRole("HR")); // alphabets only
        assertTrue(Role.isValidRole("123")); // numbers only
        assertTrue(Role.isValidRole("HR123")); // alphanumeric
        assertTrue(Role.isValidRole("h")); // single character
        assertTrue(Role.isValidRole("H")); // single uppercase
        assertTrue(Role.isValidRole("HR Department")); // contains space
        assertTrue(Role.isValidRole("12345678901234567890123456789012345678901234567890")); // exactly 50 chars
    }

    @Test
    public void equals() {
        Role role = new Role("Valid Role");

        // same values -> returns true
        assertTrue(role.equals(new Role("Valid Role")));

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
