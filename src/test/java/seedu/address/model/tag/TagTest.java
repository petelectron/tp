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
        // Valid tag names - alphanumeric, hyphens, spaces, and within length limit (30 chars)
        assertDoesNotThrow(() -> new Tag("HR"));
        assertDoesNotThrow(() -> new Tag("Department123"));
        assertDoesNotThrow(() -> new Tag("high-priority")); // with hyphen
        assertDoesNotThrow(() -> new Tag("a")); // minimum length
        assertDoesNotThrow(() -> new Tag("A")); // uppercase
        assertDoesNotThrow(() -> new Tag("123456789012345678901234567890")); // exactly 30 chars
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // --- INVALID TAG NAMES ---
        assertFalse(Tag.isValidTagName("")); // empty string
        assertFalse(Tag.isValidTagName(" ")); // spaces only
        assertFalse(Tag.isValidTagName("-")); // hyphen only
        assertFalse(Tag.isValidTagName("^")); // only non-alphanumeric characters
        assertFalse(Tag.isValidTagName("!")); // non-alphanumeric character
        assertFalse(Tag.isValidTagName("@")); // non-alphanumeric character
        assertFalse(Tag.isValidTagName("HR*")); // contains non-alphanumeric symbols
        assertFalse(Tag.isValidTagName("HR_Department")); // underscore is not allowed
        assertFalse(Tag.isValidTagName("123456789012345678901234567890123456789012345678901")); // 51 characters

        // Edge Case: Leading/Trailing separators
        assertFalse(Tag.isValidTagName(" important")); // starts with a space
        assertFalse(Tag.isValidTagName("important ")); // ends with a space
        assertFalse(Tag.isValidTagName("-important")); // starts with a hyphen
        assertFalse(Tag.isValidTagName("important-")); // ends with a hyphen

        // Edge Case: Consecutive separators
        assertFalse(Tag.isValidTagName("high  priority")); // consecutive spaces
        assertFalse(Tag.isValidTagName("high--priority")); // consecutive hyphens
        assertFalse(Tag.isValidTagName("high- priority")); // consecutive hyphen and space
        assertFalse(Tag.isValidTagName("high -priority")); // consecutive space and hyphen

        // --- VALID TAG NAMES ---
        assertTrue(Tag.isValidTagName("a")); // single character
        assertTrue(Tag.isValidTagName("A")); // single capital character
        assertTrue(Tag.isValidTagName("1")); // single number
        assertTrue(Tag.isValidTagName("HR")); // alphabets only
        assertTrue(Tag.isValidTagName("123")); // numbers only
        assertTrue(Tag.isValidTagName("Department123")); // alphanumeric
        assertTrue(Tag.isValidTagName("high priority")); // with space
        assertTrue(Tag.isValidTagName("high-priority")); // with hyphen
        assertTrue(Tag.isValidTagName("A-1 B-2")); // complex separators with alphanumeric
        assertTrue(Tag.isValidTagName("123456789012345678901234567890")); // exactly 30 chars
    }

    @Test
    public void equals() {
        Tag tag = new Tag("HR");

        // same values -> returns true
        assertTrue(tag.equals(new Tag("HR")));

        // case insensitive: different case -> returns true
        assertTrue(tag.equals(new Tag("hr")));
        assertTrue(tag.equals(new Tag("Hr")));
        assertTrue(tag.equals(new Tag("hR")));

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
        Tag tag3 = new Tag("hr");
        Tag tag4 = new Tag("Manager");

        // Same tag names should have same hash code
        assertTrue(tag1.hashCode() == tag2.hashCode());

        // Different tag names should have different hash codes
        assertFalse(tag1.hashCode() == tag4.hashCode());

        // Case-insensitive tags should have same hash code
        assertTrue(tag1.hashCode() == tag3.hashCode());
    }

    @Test
    public void toString_test() {
        Tag tag = new Tag("hr");
        assertTrue(tag.toString().equals("[hr]"));
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
