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

        // invalid departments
        assertFalse(Department.isValidDepartment(""));
        assertFalse(Department.isValidDepartment(" "));

        // valid departments
        assertTrue(Department.isValidDepartment("Human Resources"));
        assertTrue(Department.isValidDepartment("HR"));
        assertTrue(Department.isValidDepartment("Operations and Logistics"));
    }

    @Test
    public void equals() {
        Department department = new Department("Human Resources");

        assertTrue(department.equals(new Department("Human Resources")));
        assertTrue(department.equals(department));
        assertFalse(department.equals(null));
        assertFalse(department.equals(5.0f));
        assertFalse(department.equals(new Department("Finance")));
    }
}

