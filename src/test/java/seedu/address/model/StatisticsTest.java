package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Department;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

public class StatisticsTest {

    @Test
    public void constructor_nullPersonList_throwsAssertionError() {
        org.junit.jupiter.api.Assertions.assertThrows(AssertionError.class, () ->
                new Statistics(null));
    }

    @Test
    public void constructor_emptyPersonList_returnsEmptyStats() {
        List<Person> emptyList = Collections.emptyList();
        Statistics stats = new Statistics(emptyList);

        assertEquals(0, stats.getTotalEmployees());
        assertEquals(0, stats.getUniqueValueCount());
        assertEquals("None", stats.getMostCommonValue());
        assertEquals(0, stats.getEmployeesWithValue());
        assertEquals(0, stats.getEmployeesWithoutValue());
        assertEquals("No tags yet", stats.getValueDistribution());
    }

    @Test
    public void constructor_singlePersonWithoutTags_returnsCorrectStats() {
        // Create a person without tags explicitly instead of using ALICE
        Person personWithoutTags = createPersonWithTags("Alice", "Marketing"); // No tags
        List<Person> persons = Collections.singletonList(personWithoutTags);

        Statistics stats = new Statistics(persons);

        assertEquals(1, stats.getTotalEmployees());
        assertEquals(0, stats.getUniqueValueCount());
        assertEquals("None", stats.getMostCommonValue());
        assertEquals(0, stats.getEmployeesWithValue());
        assertEquals(1, stats.getEmployeesWithoutValue());
        assertEquals("No tags yet", stats.getValueDistribution());
    }

    @Test
    public void constructor_singlePersonWithTags_returnsCorrectStats() {
        Person personWithTag = createPersonWithTags("Test Person", "Marketing", "HR");
        List<Person> persons = Collections.singletonList(personWithTag);

        Statistics stats = new Statistics(persons);

        assertEquals(1, stats.getTotalEmployees());
        assertEquals(1, stats.getUniqueValueCount());
        assertEquals("hr (1)", stats.getMostCommonValue());
        assertEquals(1, stats.getEmployeesWithValue());
        assertEquals(0, stats.getEmployeesWithoutValue());
        assertEquals("• hr: 1", stats.getValueDistribution());
    }

    @Test
    public void constructor_multiplePersonsWithTags_returnsCorrectStats() {
        Person hr1 = createPersonWithTags("John", "Marketing", "HR");
        Person hr2 = createPersonWithTags("Jane", "Marketing", "HR");
        Person manager = createPersonWithTags("Bob", "Sales", "Manager");
        Person intern = createPersonWithTags("Alice", "Engineering", "Intern");
        Person noTags = createPersonWithTags("Charlie", "Marketing");

        List<Person> persons = Arrays.asList(hr1, hr2, manager, intern, noTags);
        Statistics stats = new Statistics(persons);

        assertEquals(5, stats.getTotalEmployees());
        assertEquals(3, stats.getUniqueValueCount());
        assertEquals("hr (2)", stats.getMostCommonValue());
        assertEquals(4, stats.getEmployeesWithValue());
        assertEquals(1, stats.getEmployeesWithoutValue());

        String distribution = stats.getValueDistribution();
        org.junit.jupiter.api.Assertions.assertTrue(distribution.contains("hr: 2"));
        org.junit.jupiter.api.Assertions.assertTrue(distribution.contains("manager: 1"));
        org.junit.jupiter.api.Assertions.assertTrue(distribution.contains("intern: 1"));
    }

    @Test
    public void constructor_personWithMultipleTags_countsEachTag() {
        Person multiTagPerson = createPersonWithTags("Multi", "Engineering", "HR", "Manager", "FullTime");
        List<Person> persons = Collections.singletonList(multiTagPerson);

        Statistics stats = new Statistics(persons);

        assertEquals(1, stats.getTotalEmployees());
        assertEquals(3, stats.getUniqueValueCount());
        assertEquals(1, stats.getEmployeesWithValue());
        assertEquals(0, stats.getEmployeesWithoutValue());
    }

    @Test
    public void constructor_moreThanFiveTags_showsAllTags() {
        Person personWithManyTags = createPersonWithTags("Many", "Engineering",
                "Tag1", "Tag2", "Tag3", "Tag4", "Tag5", "Tag6");
        List<Person> persons = Collections.singletonList(personWithManyTags);

        Statistics stats = new Statistics(persons);

        String distribution = stats.getValueDistribution();
        int lineCount = distribution.split("\n").length;
        assertEquals(6, lineCount);
    }

    // ---- getStatisticsMode ----

    @Test
    public void getStatisticsMode_defaultConstructor_returnsTag() {
        Statistics stats = new Statistics(Collections.emptyList());
        assertEquals(StatisticsMode.TAG, stats.getStatisticsMode());
    }

    @Test
    public void getStatisticsMode_explicitTagMode_returnsTag() {
        Statistics stats = new Statistics(Collections.emptyList(), StatisticsMode.TAG);
        assertEquals(StatisticsMode.TAG, stats.getStatisticsMode());
    }

    @Test
    public void getStatisticsMode_departmentMode_returnsDepartment() {
        Statistics stats = new Statistics(Collections.emptyList(), StatisticsMode.DEPARTMENT);
        assertEquals(StatisticsMode.DEPARTMENT, stats.getStatisticsMode());
    }

    // ---- null-mode assertion ----

    @Test
    public void constructor_nullMode_throwsAssertionError() {
        org.junit.jupiter.api.Assertions.assertThrows(AssertionError.class, () ->
                new Statistics(Collections.emptyList(), null));
    }

    // ---- DEPARTMENT mode: empty list ----

    @Test
    public void departmentMode_emptyPersonList_returnsEmptyStats() {
        Statistics stats = new Statistics(Collections.emptyList(), StatisticsMode.DEPARTMENT);

        assertEquals(0, stats.getTotalEmployees());
        assertEquals(0, stats.getUniqueValueCount());
        assertEquals("None", stats.getMostCommonValue());
        assertEquals(0, stats.getEmployeesWithValue());
        assertEquals(0, stats.getEmployeesWithoutValue());
        assertEquals("No departments yet", stats.getValueDistribution());
    }

    // ---- DEPARTMENT mode: single person with a department ----

    @Test
    public void departmentMode_singlePersonWithDepartment_returnsCorrectStats() {
        Person person = createPersonWithDepartment("Alice Tan", "Engineering");
        Statistics stats = new Statistics(Collections.singletonList(person), StatisticsMode.DEPARTMENT);

        assertEquals(1, stats.getTotalEmployees());
        assertEquals(1, stats.getUniqueValueCount());
        assertEquals("engineering (1)", stats.getMostCommonValue());
        assertEquals(1, stats.getEmployeesWithValue());
        assertEquals(0, stats.getEmployeesWithoutValue());
        assertEquals("• engineering: 1", stats.getValueDistribution());
    }

    // ---- DEPARTMENT mode: multiple persons ----

    @Test
    public void departmentMode_multiplePersons_returnsCorrectDistribution() {
        List<Person> persons = Arrays.asList(
                createPersonWithDepartment("Alice Tan", "Engineering"),
                createPersonWithDepartment("Bob Lim", "Engineering"),
                createPersonWithDepartment("Carol Ng", "Marketing"),
                createPersonWithDepartment("David Lee", "Marketing")
        );
        Statistics stats = new Statistics(persons, StatisticsMode.DEPARTMENT);

        assertEquals(4, stats.getTotalEmployees());
        assertEquals(2, stats.getUniqueValueCount());
        assertEquals(4, stats.getEmployeesWithValue());
        assertEquals(0, stats.getEmployeesWithoutValue());

        String dist = stats.getValueDistribution();
        org.junit.jupiter.api.Assertions.assertTrue(dist.contains("engineering: 2"));
        org.junit.jupiter.api.Assertions.assertTrue(dist.contains("marketing: 2"));
    }

    // ---- DEPARTMENT mode: tie-breaking sorted alphabetically ----

    @Test
    public void departmentMode_tieInFrequency_distributionSortedAlphabetically() {
        List<Person> persons = Arrays.asList(
                createPersonWithDepartment("Ethan Ong", "Zebra"),
                createPersonWithDepartment("Felix Goh", "Alpha")
        );
        Statistics stats = new Statistics(persons, StatisticsMode.DEPARTMENT);

        String dist = stats.getValueDistribution();
        org.junit.jupiter.api.Assertions.assertTrue(
                dist.indexOf("alpha") < dist.indexOf("zebra"),
                "Equal-frequency departments should be sorted alphabetically");
    }

    // Helper method to create Person with a specific department (no tags)
    private Person createPersonWithDepartment(String name, String department) {
        String emailName = name.toLowerCase().replaceAll("[^a-z0-9]", "x");
        return new Person(
                new Name(name),
                new Phone("12345678"),
                new Email(emailName + "@example.com"),
                new Role("Employee"),
                new Department(department),
                new HashSet<>()
        );
    }

    // Helper method to create Person with tags
    private Person createPersonWithTags(String name, String department, String... tagNames) {
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
                new Department(department),
                tags
        );
    }
}
