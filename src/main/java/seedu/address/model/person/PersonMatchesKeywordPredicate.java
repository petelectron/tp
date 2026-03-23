package seedu.address.model.person;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s fields contain all of the keywords given.
 */
public class PersonMatchesKeywordPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public PersonMatchesKeywordPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        boolean allKeywordsBlank = keywords.stream().allMatch(String::isBlank);
        if (allKeywordsBlank) {
            return false;
        }

        return keywords.stream()
                .filter(keyword -> !keyword.isBlank())
                .allMatch(keyword -> matchesAnyPersonField(person, keyword));
    }

    private boolean matchesAnyPersonField(Person person, String keyword) {
        return containsIgnoreCase(person.getName().fullName, keyword)
                || containsIgnoreCase(person.getPhone().value, keyword)
                || containsIgnoreCase(person.getEmail().value, keyword)
                || containsIgnoreCase(person.getRole().value, keyword)
                || person.getTags().stream().anyMatch(tag -> containsIgnoreCase(tag.tagName, keyword));
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonMatchesKeywordPredicate)) {
            return false;
        }

        PersonMatchesKeywordPredicate otherPersonMatchesKeywordPredicate = (PersonMatchesKeywordPredicate) other;
        return keywords.equals(otherPersonMatchesKeywordPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
