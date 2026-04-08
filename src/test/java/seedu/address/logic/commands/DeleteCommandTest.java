package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                personToDelete.getName().getFullName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        expectedModel.commitAddressBook();

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(List.of(outOfBoundIndex));

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                personToDelete.getName().getFullName());

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        expectedModel.commitAddressBook();
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of HRmanager list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(List.of(outOfBoundIndex));

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));
        DeleteCommand deleteSecondCommand = new DeleteCommand(List.of(INDEX_SECOND_PERSON));

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(List.of(INDEX_FIRST_PERSON));
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(List.of(targetIndex));
        String expected = DeleteCommand.class.getCanonicalName() + "{targetIndexes=[" + targetIndex + "]}";
        assertEquals(expected, deleteCommand.toString());
    }

    @Test
    public void execute_multipleValidIndexesUnfilteredList_success() {
        List<Person> personList = model.getFilteredPersonList();
        Person personToDelete1 = personList.get(INDEX_FIRST_PERSON.getZeroBased());
        Person personToDelete2 = personList.get(INDEX_SECOND_PERSON.getZeroBased());

        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                personToDelete1.getName().getFullName() + ", " + personToDelete2.getName().getFullName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete1);
        expectedModel.deletePerson(personToDelete2);
        expectedModel.commitAddressBook();

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateIndexes_success() {
        List<Person> personList = model.getFilteredPersonList();
        Person personToDelete = personList.get(INDEX_FIRST_PERSON.getZeroBased());

        // Pass the same index twice - duplicates are now filtered out
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON));

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                personToDelete.getName().getFullName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        expectedModel.commitAddressBook();

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleIndexesReverseOrder_success() {
        List<Person> personList = model.getFilteredPersonList();
        Person personToDelete1 = personList.get(INDEX_FIRST_PERSON.getZeroBased());
        Person personToDelete2 = personList.get(INDEX_SECOND_PERSON.getZeroBased());

        // Pass indexes in reverse order to test sorting logic
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON));

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                personToDelete1.getName().getFullName() + ", " + personToDelete2.getName().getFullName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete1);
        expectedModel.deletePerson(personToDelete2);
        expectedModel.commitAddressBook();

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleIndexesWithDuplicates_success() {
        List<Person> personList = model.getFilteredPersonList();
        Person personToDelete1 = personList.get(INDEX_FIRST_PERSON.getZeroBased());
        Person personToDelete2 = personList.get(INDEX_SECOND_PERSON.getZeroBased());
        Person personToDelete3 = personList.get(INDEX_THIRD_PERSON.getZeroBased());

        // Pass multiple indexes with some duplicates (4 indexes, but only 3 unique)
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_THIRD_PERSON, INDEX_FIRST_PERSON,
                INDEX_SECOND_PERSON, INDEX_FIRST_PERSON));

        // Duplicates are filtered out, so message correctly reports 3 unique employees deleted
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                personToDelete1.getName().getFullName() + ", "
                        + personToDelete2.getName().getFullName() + ", "
                        + personToDelete3.getName().getFullName());

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        // 3 unique persons are deleted
        expectedModel.deletePerson(personToDelete1);
        expectedModel.deletePerson(personToDelete2);
        expectedModel.deletePerson(personToDelete3);
        expectedModel.commitAddressBook();

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_oneValidOneInvalidIndexMultiple_throwsCommandException() {
        Index validIndex = INDEX_FIRST_PERSON;
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        DeleteCommand deleteCommand = new DeleteCommand(List.of(validIndex, outOfBoundIndex));

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void getConfirmationPrompt_duplicateIndexes_reportsUniqueCount() {
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON));
        String expectedPrompt = ConfirmationPromptFormatter.format(
                String.format(DeleteCommand.ACTION_SUMMARY_FORMAT, 1),
                DeleteCommand.IMPACT_SUMMARY);
        assertEquals(expectedPrompt, deleteCommand.getConfirmationPrompt());
    }

    @Test
    public void getActionDescription_returnsExpectedDescription() {
        DeleteCommand deleteCommand = new DeleteCommand(List.of(INDEX_FIRST_PERSON));
        assertEquals(DeleteCommand.ACTION_DESCRIPTION, deleteCommand.getActionDescription());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
