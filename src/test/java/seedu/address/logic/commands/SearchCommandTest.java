package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;
import static seedu.address.testutil.TypicalPersons.getTypicalPersons;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.PersonMatchesKeywordPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code SearchCommand}.
 */
public class SearchCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SearchCommand(null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        PersonMatchesKeywordPredicate predicate = new PersonMatchesKeywordPredicate(
                Collections.singletonList("keyword"));
        SearchCommand searchCommand = new SearchCommand(predicate);
        assertThrows(NullPointerException.class, () -> searchCommand.execute(null));
    }

    @Test
    public void equals() {
        PersonMatchesKeywordPredicate firstPredicate =
            new PersonMatchesKeywordPredicate(Collections.singletonList("first"));
        PersonMatchesKeywordPredicate secondPredicate =
            new PersonMatchesKeywordPredicate(Collections.singletonList("second"));

        SearchCommand searchFirstCommand = new SearchCommand(firstPredicate);
        SearchCommand searchSecondCommand = new SearchCommand(secondPredicate);

        // same object -> returns true
        assertTrue(searchFirstCommand.equals(searchFirstCommand));

        // same values -> returns true
        SearchCommand searchFirstCommandCopy = new SearchCommand(firstPredicate);
        assertTrue(searchFirstCommand.equals(searchFirstCommandCopy));

        // different types -> returns false
        assertFalse(searchFirstCommand.equals(1));

        // null -> returns false
        assertFalse(searchFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(searchFirstCommand.equals(searchSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(SearchCommand.MESSAGE_EMPLOYEES_LISTED_OVERVIEW, 0);
        PersonMatchesKeywordPredicate predicate = preparePredicate(" ");
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_noMatches_zeroEmployeesListed() {
        String expectedMessage = String.format(SearchCommand.MESSAGE_EMPLOYEES_LISTED_OVERVIEW, 0);
        PersonMatchesKeywordPredicate predicate = preparePredicate("zzznotfound");
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(SearchCommand.MESSAGE_EMPLOYEES_LISTED_OVERVIEW, 2);
        PersonMatchesKeywordPredicate predicate = preparePredicate("Ku");
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleKeyword_caseInsensitiveMultiplePersonsFound() {
        String expectedMessage = String.format(SearchCommand.MESSAGE_EMPLOYEES_LISTED_OVERVIEW, 2);
        PersonMatchesKeywordPredicate predicate = preparePredicate("ku");
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void execute_singleKeyword_matchesEmailField() {
        String expectedMessage = String.format(SearchCommand.MESSAGE_EMPLOYEES_LISTED_OVERVIEW, 7);
        PersonMatchesKeywordPredicate predicate = preparePredicate("examp");
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(getTypicalPersons(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleKeywords_orSearchAcrossFields() {
        String expectedMessage = String.format(SearchCommand.MESSAGE_EMPLOYEES_LISTED_OVERVIEW, 7);
        PersonMatchesKeywordPredicate predicate = preparePredicate("Ku examp");
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(getTypicalPersons(), model.getFilteredPersonList());
    }

    @Test
    public void execute_specialCharacterKeyword_matchesEmailField() {
        String expectedMessage = String.format(SearchCommand.MESSAGE_EMPLOYEES_LISTED_OVERVIEW, 7);
        PersonMatchesKeywordPredicate predicate = preparePredicate("@");
        SearchCommand command = new SearchCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(getTypicalPersons(), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        PersonMatchesKeywordPredicate predicate = new PersonMatchesKeywordPredicate(Arrays.asList("keyword"));
        SearchCommand searchCommand = new SearchCommand(predicate);
        String expected = SearchCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, searchCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code PersonMatchesKeywordPredicate}.
     */
    private PersonMatchesKeywordPredicate preparePredicate(String userInput) {
        return new PersonMatchesKeywordPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
