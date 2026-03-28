package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class PersonCardTest {

    @BeforeAll
    public static void setUp() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // Toolkit already initialized
        }
    }

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PersonCard(null, 0));
    }

    @Test
    public void constructor_validPerson_success() {
        Person validPerson = new PersonBuilder().build();
        PersonCard personCard = new PersonCard(validPerson, 1);
        assertNotNull(personCard);
    }

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        Person missingAttributePerson = new PersonBuilder().build();
        Person testPerson = new Person(
                missingAttributePerson.getName(),
                missingAttributePerson.getPhone(),
                missingAttributePerson.getEmail(),
                missingAttributePerson.getRole(),
                missingAttributePerson.getDepartment(),
                missingAttributePerson.getTags()) {
            @Override
            public seedu.address.model.person.Name getName() {
                return null;
            }
        };
        assertThrows(NullPointerException.class, () -> new PersonCard(testPerson, 1));
    }

    @Test
    public void constructor_nullPhone_throwsNullPointerException() {
        Person missingAttributePerson = new PersonBuilder().build();
        Person testPerson = new Person(
                missingAttributePerson.getName(),
                missingAttributePerson.getPhone(),
                missingAttributePerson.getEmail(),
                missingAttributePerson.getRole(),
                missingAttributePerson.getDepartment(),
                missingAttributePerson.getTags()) {
            @Override
            public seedu.address.model.person.Phone getPhone() {
                return null;
            }
        };
        assertThrows(NullPointerException.class, () -> new PersonCard(testPerson, 1));
    }

    @Test
    public void constructor_nullRole_throwsNullPointerException() {
        Person missingAttributePerson = new PersonBuilder().build();
        Person testPerson = new Person(
                missingAttributePerson.getName(),
                missingAttributePerson.getPhone(),
                missingAttributePerson.getEmail(),
                missingAttributePerson.getRole(),
                missingAttributePerson.getDepartment(),
                missingAttributePerson.getTags()) {
            @Override
            public seedu.address.model.person.Role getRole() {
                return null;
            }
        };
        assertThrows(NullPointerException.class, () -> new PersonCard(testPerson, 1));
    }

    @Test
    public void constructor_nullDepartment_throwsNullPointerException() {
        Person missingAttributePerson = new PersonBuilder().build();
        Person testPerson = new Person(
                missingAttributePerson.getName(),
                missingAttributePerson.getPhone(),
                missingAttributePerson.getEmail(),
                missingAttributePerson.getRole(),
                missingAttributePerson.getDepartment(),
                missingAttributePerson.getTags()) {
            @Override
            public seedu.address.model.person.Department getDepartment() {
                return null;
            }
        };
        assertThrows(NullPointerException.class, () -> new PersonCard(testPerson, 1));
    }

    @Test
    public void constructor_nullEmail_throwsNullPointerException() {
        Person missingAttributePerson = new PersonBuilder().build();
        Person testPerson = new Person(
                missingAttributePerson.getName(),
                missingAttributePerson.getPhone(),
                missingAttributePerson.getEmail(),
                missingAttributePerson.getRole(),
                missingAttributePerson.getDepartment(),
                missingAttributePerson.getTags()) {
            @Override
            public seedu.address.model.person.Email getEmail() {
                return null;
            }
        };
        assertThrows(NullPointerException.class, () -> new PersonCard(testPerson, 1));
    }

    @Test
    public void constructor_nullTags_throwsNullPointerException() {
        Person missingAttributePerson = new PersonBuilder().build();
        Person testPerson = new Person(
                missingAttributePerson.getName(),
                missingAttributePerson.getPhone(),
                missingAttributePerson.getEmail(),
                missingAttributePerson.getRole(),
                missingAttributePerson.getDepartment(),
                missingAttributePerson.getTags()) {
            @Override
            public java.util.Set<seedu.address.model.tag.Tag> getTags() {
                return null;
            }
        };
        assertThrows(NullPointerException.class, () -> new PersonCard(testPerson, 1));
    }
}
