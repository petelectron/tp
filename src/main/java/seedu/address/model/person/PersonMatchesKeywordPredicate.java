package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s fields contain at least one of the keywords given.
 */
public class PersonMatchesKeywordPredicate implements Predicate<Person> {
    private final List<String> keywords;

    /**
     * Constructs a {@code PersonMatchesKeywordPredicate} with the specified keywords.
     *
     * @param keywords The list of keywords to test against.
     */
    public PersonMatchesKeywordPredicate(List<String> keywords) {
        requireNonNull(keywords);
        this.keywords = List.copyOf(keywords);
    }

    /**
     * Returns whether the given {@code person} matches at least one non-blank keyword.
     *
     * @param person Person to test.
     * @return {@code true} if any keyword matches a searchable field in the person.
     */
    @Override
    public boolean test(Person person) {
        requireNonNull(person);

        boolean areAllKeywordsBlank = keywords.stream().allMatch(String::isBlank);
        if (areAllKeywordsBlank) {
            return false;
        }

        return keywords.stream()
                .filter(keyword -> !keyword.isBlank())
            .anyMatch(keyword -> doesMatchAnyPersonField(person, keyword));
    }

    private boolean doesMatchAnyPersonField(Person person, String keyword) {
        return containsIgnoreCase(person.getName().getFullName(), keyword)
                || containsIgnoreCase(person.getPhone().getValue(), keyword)
                || containsIgnoreCase(person.getEmail().getValue(), keyword)
                || containsIgnoreCase(person.getRole().getValue(), keyword)
                || containsIgnoreCase(person.getDepartment().getValue(), keyword)
                || person.getTags().stream().anyMatch(tag -> containsIgnoreCase(tag.getTagName(), keyword));
    }

    private boolean containsIgnoreCase(String value, String keyword) {
        return value.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    /**
     * Returns whether this predicate is equal to another object.
     *
     * @param other Object to compare against.
     * @return {@code true} if both predicates contain the same keywords.
     */
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

    /**
     * Returns a string representation of this predicate.
     *
     * @return String form containing the configured keywords.
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
