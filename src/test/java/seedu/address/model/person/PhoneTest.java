package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers

        // empty string
        assertFalse(Phone.isValidPhone(""));
        // spaces only
        assertFalse(Phone.isValidPhone(" "));
        // less than 3 numbers
        assertFalse(Phone.isValidPhone("91"));
        // non-numeric
        assertFalse(Phone.isValidPhone("phone"));
        // alphabets within digits
        assertFalse(Phone.isValidPhone("9011p041"));
        // spaces within digits
        assertFalse(Phone.isValidPhone("9312 1534"));
        // start with special char (simulate extension)
        assertFalse(Phone.isValidPhone("+621859312"));
        // special char within digits
        assertFalse(Phone.isValidPhone("62/1859312"));
        // spaces within digits (simulate country code)
        assertFalse(Phone.isValidPhone("62 185 9312"));
        // special char within digits (simulate extension)
        assertFalse(Phone.isValidPhone("+621859312"));
        // spaces within digits (simulate country code)
        assertFalse(Phone.isValidPhone("62 1249312"));
        // special character '!'
        assertFalse(Phone.isValidPhone("931!1534"));
        // special character '#'
        assertFalse(Phone.isValidPhone("93#141534")); 
        // len 17 phone number (>16)
        assertFalse(Phone.isValidPhone("93122315678111534"));
        // ridiculous length phone number
        assertFalse(Phone.isValidPhone("356762194720127421646129468281"));

        // valid phone numbers

        // exactly 3 numbers
        assertTrue(Phone.isValidPhone("911"));
        // reasonable normal phone number
        assertTrue(Phone.isValidPhone("93121534"));
        // len 15 long phone number
        assertTrue(Phone.isValidPhone("124293842033123"));
        // len 16 phone number; possible edge case failure at limit
        assertTrue(Phone.isValidPhone("9312231567811153")); 
    }

    @Test
    public void equals() {
        Phone phone = new Phone("999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("995")));
    }
}
