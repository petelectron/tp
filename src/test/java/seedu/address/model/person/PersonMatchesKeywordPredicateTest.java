package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class PersonMatchesKeywordPredicateTest {

    @Test
    public void constructor_nullKeywordsList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PersonMatchesKeywordPredicate(null));
    }

    @Test
    public void test_nullPerson_throwsNullPointerException() {
        PersonMatchesKeywordPredicate predicate = new PersonMatchesKeywordPredicate(Collections.singletonList("Alice"));
        assertThrows(NullPointerException.class, () -> predicate.test(null));
    }

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = Collections.singletonList("first");
        List<String> secondPredicateKeywordList = Arrays.asList("first", "second");

        PersonMatchesKeywordPredicate firstPredicate = new PersonMatchesKeywordPredicate(firstPredicateKeywordList);
        PersonMatchesKeywordPredicate secondPredicate = new PersonMatchesKeywordPredicate(secondPredicateKeywordList);

        assertTrue(firstPredicate.equals(firstPredicate));

        PersonMatchesKeywordPredicate firstPredicateCopy = new PersonMatchesKeywordPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        assertFalse(firstPredicate.equals(1));

        assertFalse(firstPredicate.equals(null));

        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_personContainsAnyKeyword_returnsTrue() {
        PersonMatchesKeywordPredicate predicate =
                new PersonMatchesKeywordPredicate(Collections.singletonList("Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("Alice", "Bob"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("aLIce", "bOB"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("lic", "aro"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Carol").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("aLI", "bO"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("Alice", "   "));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("Carol", "Alice"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("Alice", "12345", "friend"));
        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").withRole("UI Designer").withTags("friend").build()));
    }

    @Test
    public void test_personContainsNoKeyword_returnsFalse() {
        PersonMatchesKeywordPredicate predicate = new PersonMatchesKeywordPredicate(Collections.emptyList());
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("   ", ""));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("Carol"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("Carol", "xyz"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("notpresent", "   "));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice Bob").build()));

        predicate = new PersonMatchesKeywordPredicate(Arrays.asList("notpresent"));
        assertFalse(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").withRole("UI Designer").withTags("friend").build()));
    }

    @Test
    public void test_nonNameFieldsContainKeywords_returnsTrue() {
        PersonMatchesKeywordPredicate predicate =
                new PersonMatchesKeywordPredicate(Arrays.asList("12345", "email", "designer", "friend"));

        assertTrue(predicate.test(new PersonBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").withRole("UI Designer").withTags("friend").build()));
    }

    @Test
    public void test_eachFieldMatchForBranchCoverage_returnsTrue() {
        Person person = new PersonBuilder().withName("Alice Bob").withPhone("91234567")
                .withEmail("alice@email.com").withRole("UI Designer").withDepartment("Platform Ops")
                .withTags("friend").build();

        assertTrue(new PersonMatchesKeywordPredicate(Collections.singletonList("Alice")).test(person));
        assertTrue(new PersonMatchesKeywordPredicate(Collections.singletonList("9123")).test(person));
        assertTrue(new PersonMatchesKeywordPredicate(Collections.singletonList("email.com")).test(person));
        assertTrue(new PersonMatchesKeywordPredicate(Collections.singletonList("Designer")).test(person));
        assertTrue(new PersonMatchesKeywordPredicate(Collections.singletonList("Platform")).test(person));
        assertTrue(new PersonMatchesKeywordPredicate(Collections.singletonList("friend")).test(person));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PersonMatchesKeywordPredicate predicate = new PersonMatchesKeywordPredicate(keywords);

        String expected = PersonMatchesKeywordPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
