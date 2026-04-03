package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class DepartmentTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Department(null));
    }

    @Test
    public void constructor_invalidDepartment_throwsIllegalArgumentException() {
        String invalidDepartment = "";
        assertThrows(IllegalArgumentException.class, () -> new Department(invalidDepartment));
    }

    @Test
    public void isValidDepartment() {
        // null department
        assertThrows(NullPointerException.class, () -> Department.isValidDepartment(null));

        // --- INVALID DEPARTMENTS ---
        assertFalse(Department.isValidDepartment("")); // empty string
        assertFalse(Department.isValidDepartment(" ")); // spaces only
        assertFalse(Department.isValidDepartment("-")); // hyphen only
        assertFalse(Department.isValidDepartment("^")); // only non-alphanumeric characters
        assertFalse(Department.isValidDepartment("dept*")); // contains non-alphanumeric symbols
        assertFalse(Department.isValidDepartment("dept_sales")); // underscore is not allowed
        assertFalse(Department.isValidDepartment("dept@sales")); // special characters

        // Edge Case: Leading/Trailing separators
        assertFalse(Department.isValidDepartment(" HR Department")); // starts with a space
        assertFalse(Department.isValidDepartment("HR Department ")); // ends with a space
        assertFalse(Department.isValidDepartment("-HR Department")); // starts with a hyphen
        assertFalse(Department.isValidDepartment("HR Department-")); // ends with a hyphen

        // Edge Case: Consecutive separators
        assertFalse(Department.isValidDepartment("HR  Department")); // consecutive spaces
        assertFalse(Department.isValidDepartment("HR--Department")); // consecutive hyphens
        assertFalse(Department.isValidDepartment("HR- Department")); // consecutive hyphen and space
        assertFalse(Department.isValidDepartment("HR -Department")); // consecutive space and hyphen

        // Length (Max 30)
        assertFalse(Department.isValidDepartment("Human Resources Management Division")); // 35 characters


        // --- VALID DEPARTMENTS ---
        assertTrue(Department.isValidDepartment("a")); // single character
        assertTrue(Department.isValidDepartment("A")); // single capital character
        assertTrue(Department.isValidDepartment("1")); // single number
        assertTrue(Department.isValidDepartment("HR")); // alphabets only
        assertTrue(Department.isValidDepartment("123")); // numbers only
        assertTrue(Department.isValidDepartment("HR Department")); // with space
        assertTrue(Department.isValidDepartment("Human Resources")); // alphanumeric characters
        assertTrue(Department.isValidDepartment("HR-Department")); // with hyphen
        assertTrue(Department.isValidDepartment("Sales-Support 3")); // complex separators with alphanumeric
        assertTrue(Department.isValidDepartment("123456789012345678901234567890")); // exactly 30 chars
        assertTrue(Department.isValidDepartment("Human Resources Management Dv4")); // 30 characters
    }

    @Test
    public void equals() {
        Department department = new Department("Human Resources");

        // same values -> returns true
        assertTrue(department.equals(new Department("Human Resources")));

        // case insensitive: different case -> returns true
        assertTrue(department.equals(new Department("human resources")));
        assertTrue(department.equals(new Department("Human resources")));
        assertTrue(department.equals(new Department("HUMAN RESOURCES")));

        // same object -> returns true
        assertTrue(department.equals(department));

        // null -> returns false
        assertFalse(department.equals(null));

        // different types -> returns false
        assertFalse(department.equals(5.0f));

        // different values -> returns false
        assertFalse(department.equals(new Department("Finance")));
    }
}

