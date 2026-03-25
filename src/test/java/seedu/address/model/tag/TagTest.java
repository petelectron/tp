package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void constructor_validTagName_success() {
        // Valid tag names - alphanumeric and within length limit
        assertDoesNotThrow(() -> new Tag("HR"));
        assertDoesNotThrow(() -> new Tag("Department123"));
        assertDoesNotThrow(() -> new Tag("a")); // minimum length
        assertDoesNotThrow(() -> new Tag("A")); // uppercase
        assertDoesNotThrow(() -> new Tag("12345678901234567890123456789012345678901234567890")); // exactly 50 chars
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag names
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName(" ")); // spaces only
        assertFalse(Tag.isValidTagName("^")); // non-alphanumeric character
        assertFalse(Tag.isValidTagName("!")); // non-alphanumeric character
        assertFalse(Tag.isValidTagName("@")); // non-alphanumeric character
        assertFalse(Tag.isValidTagName("HR*")); // contains  non-alphanumeric character
        assertFalse(Tag.isValidTagName("HR_Department")); // contains underscore non-alphanumeric character
        assertFalse(Tag.isValidTagName("HR-Department")); // contains hyphen non-alphanumeric character
        assertFalse(Tag.isValidTagName("123456789012345678901234567890123456789012345678901")); // 51 characters

        // valid tag names
        assertTrue(Tag.isValidTagName("HR")); // alphabets only
        assertTrue(Tag.isValidTagName("123")); // numbers only
        assertTrue(Tag.isValidTagName("HR123")); // alphanumeric
        assertTrue(Tag.isValidTagName("h")); // single character
        assertTrue(Tag.isValidTagName("H")); // single uppercase
        assertTrue(Tag.isValidTagName("HR Department")); // contains space
        assertTrue(Tag.isValidTagName("12345678901234567890123456789012345678901234567890")); // exactly 50 chars
    }

    @Test
    public void equals() {
        Tag tag = new Tag("HR");

        // same values -> returns true
        assertTrue(tag.equals(new Tag("HR")));

        // same object -> returns true
        assertTrue(tag.equals(tag));

        // null -> returns false
        assertFalse(tag.equals(null));

        // different types -> returns false
        assertFalse(tag.equals(5.0f));

        // different values -> returns false
        assertFalse(tag.equals(new Tag("Manager")));
    }

    @Test
    public void hashCode_test() {
        Tag tag1 = new Tag("HR");
        Tag tag2 = new Tag("HR");
        Tag tag3 = new Tag("Manager");

        // Same tag names should have same hash code
        assertTrue(tag1.hashCode() == tag2.hashCode());

        // Different tag names should have different hash codes
        assertFalse(tag1.hashCode() == tag3.hashCode());
    }

    @Test
    public void toString_test() {
        Tag tag = new Tag("HR");
        assertTrue(tag.toString().equals("[HR]"));
    }

    /**
     * Helper method to assert that a constructor call does not throw an exception.
     */
    private void assertDoesNotThrow(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            throw new AssertionError("Expected no exception, but threw: " + e.getMessage(), e);
        }
    }
}
