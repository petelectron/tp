package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

public class StatisticsTest {

    @Test
    public void constructor_nullPersonList_throwsAssertionError() {
        org.junit.jupiter.api.Assertions.assertThrows(AssertionError.class, () -> {
            new Statistics(null);
        });
    }

    @Test
    public void constructor_emptyPersonList_returnsEmptyStats() {
        List<Person> emptyList = Collections.emptyList();
        Statistics stats = new Statistics(emptyList);

        assertEquals(0, stats.getTotalEmployees());
        assertEquals(0, stats.getUniqueTagCount());
        assertEquals("None", stats.getMostCommonTag());
        assertEquals(0, stats.getEmployeesWithTags());
        assertEquals(0, stats.getEmployeesWithoutTags());
        assertEquals("No tags yet", stats.getTagDistribution());
    }

    @Test
    public void constructor_singlePersonWithoutTags_returnsCorrectStats() {
        // Create a person without tags explicitly instead of using ALICE
        Person personWithoutTags = createPersonWithTags("Alice"); // No tags
        List<Person> persons = Collections.singletonList(personWithoutTags);

        Statistics stats = new Statistics(persons);

        assertEquals(1, stats.getTotalEmployees());
        assertEquals(0, stats.getUniqueTagCount());
        assertEquals("None", stats.getMostCommonTag());
        assertEquals(0, stats.getEmployeesWithTags());
        assertEquals(1, stats.getEmployeesWithoutTags());
        assertEquals("No tags yet", stats.getTagDistribution());
    }

    @Test
    public void constructor_singlePersonWithTags_returnsCorrectStats() {
        Person personWithTag = createPersonWithTags("Test Person", "HR");
        List<Person> persons = Collections.singletonList(personWithTag);

        Statistics stats = new Statistics(persons);

        assertEquals(1, stats.getTotalEmployees());
        assertEquals(1, stats.getUniqueTagCount());
        assertEquals("HR (1)", stats.getMostCommonTag());
        assertEquals(1, stats.getEmployeesWithTags());
        assertEquals(0, stats.getEmployeesWithoutTags());
        assertEquals("• HR: 1", stats.getTagDistribution());
    }

    @Test
    public void constructor_multiplePersonsWithTags_returnsCorrectStats() {
        Person hr1 = createPersonWithTags("John", "HR");
        Person hr2 = createPersonWithTags("Jane", "HR");
        Person manager = createPersonWithTags("Bob", "Manager");
        Person intern = createPersonWithTags("Alice", "Intern");
        Person noTags = createPersonWithTags("Charlie");

        List<Person> persons = Arrays.asList(hr1, hr2, manager, intern, noTags);
        Statistics stats = new Statistics(persons);

        assertEquals(5, stats.getTotalEmployees());
        assertEquals(3, stats.getUniqueTagCount());
        assertEquals("HR (2)", stats.getMostCommonTag());
        assertEquals(4, stats.getEmployeesWithTags());
        assertEquals(1, stats.getEmployeesWithoutTags());

        String distribution = stats.getTagDistribution();
        org.junit.jupiter.api.Assertions.assertTrue(distribution.contains("HR: 2"));
        org.junit.jupiter.api.Assertions.assertTrue(distribution.contains("Manager: 1"));
        org.junit.jupiter.api.Assertions.assertTrue(distribution.contains("Intern: 1"));
    }

    @Test
    public void constructor_personWithMultipleTags_countsEachTag() {
        Person multiTagPerson = createPersonWithTags("Multi", "HR", "Manager", "FullTime");
        List<Person> persons = Collections.singletonList(multiTagPerson);

        Statistics stats = new Statistics(persons);

        assertEquals(1, stats.getTotalEmployees());
        assertEquals(3, stats.getUniqueTagCount());
        assertEquals("HR (1)", stats.getMostCommonTag());
        assertEquals(1, stats.getEmployeesWithTags());
        assertEquals(0, stats.getEmployeesWithoutTags());
    }

    @Test
    public void constructor_moreThanFiveTags_showsOnlyTopFive() {
        Person personWithManyTags = createPersonWithTags("Many",
                "Tag1", "Tag2", "Tag3", "Tag4", "Tag5", "Tag6");
        List<Person> persons = Collections.singletonList(personWithManyTags);

        Statistics stats = new Statistics(persons);

        String distribution = stats.getTagDistribution();
        int lineCount = distribution.split("\n").length;
        assertEquals(5, lineCount);
    }

    // Helper method to create Person with tags
    private Person createPersonWithTags(String name, String... tagNames) {
        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            tags.add(new Tag(tagName));
        }

        String emailName = name.toLowerCase().replaceAll("[^a-z0-9]", "");

        return new Person(
                new Name(name),
                new Phone("12345678"),
                new Email(emailName + "@example.com"),
                new Role("Employee"),
                tags
        );
    }
}
