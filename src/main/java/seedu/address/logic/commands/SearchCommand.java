package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.PersonMatchesKeywordPredicate;

/**
 * Searches and lists all employees whose fields contain at least one argument keyword.
 * Keyword matching is case insensitive.
 */
public class SearchCommand extends Command {

    public static final String COMMAND_WORD = "search";
    public static final int MAX_KEYWORDS = 5;
    public static final int MAX_KEYWORD_LENGTH = 50;
    public static final String MESSAGE_EMPLOYEES_LISTED_OVERVIEW = "%1$d employees listed!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Searches all employees whose fields contain at "
        + "least one "
        + "specified keywords (case-insensitive) and displays them as a list with index numbers.\n"
        + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
        + "Constraints: 1 to " + MAX_KEYWORDS + " keywords, each keyword must contain non-whitespace "
        + "characters only and be at most "
        + MAX_KEYWORD_LENGTH + " characters.\n"
        + "Example: " + COMMAND_WORD + " alice@company.com eng-team";

    private final PersonMatchesKeywordPredicate predicate;

    /**
     * Creates a SearchCommand to find employees using the specified {@code predicate}.
     */
    public SearchCommand(PersonMatchesKeywordPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        model.updateFilteredPersonList(predicate);
        int matchedEmployees = model.getFilteredPersonList().size();
        return new CommandResult(String.format(MESSAGE_EMPLOYEES_LISTED_OVERVIEW, matchedEmployees));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SearchCommand)) {
            return false;
        }

        SearchCommand otherSearchCommand = (SearchCommand) other;
        return predicate.equals(otherSearchCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
