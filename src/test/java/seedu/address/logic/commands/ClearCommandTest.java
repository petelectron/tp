package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.UndoCommand.MESSAGE_SUCCESS;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class ClearCommandTest {

    @Test
    public void execute_emptyAddressBook_success() {
        Model model = new ModelManager();
        Model expectedModel = new ModelManager();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_nonEmptyAddressBook_success() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel.setAddressBook(new AddressBook());
        expectedModel.commitAddressBook();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_clearThenUndoRestoresPreviousState() throws Exception {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Model expectedModelAfterClear = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModelAfterClear.setAddressBook(new AddressBook());
        expectedModelAfterClear.commitAddressBook();

        assertCommandSuccess(new ClearCommand(), model, ClearCommand.MESSAGE_SUCCESS, expectedModelAfterClear);
        assertTrue(model.canUndoAddressBook());

        CommandResult result = new UndoCommand().execute(model);
        assertEquals(MESSAGE_SUCCESS, result.getFeedbackToUser());

        assertEquals(new AddressBook(getTypicalAddressBook()), model.getAddressBook());
    }

    @Test
    public void getConfirmationPrompt_returnsExpectedPrompt() {
        ClearCommand clearCommand = new ClearCommand();
        assertEquals(
            ConfirmationPromptFormatter.format(
                ClearCommand.ACTION_SUMMARY,
                ClearCommand.IMPACT_SUMMARY),
            clearCommand.getConfirmationPrompt());
    }

    @Test
    public void getActionDescription_returnsExpectedDescription() {
        ClearCommand clearCommand = new ClearCommand();
        assertEquals(ClearCommand.ACTION_DESCRIPTION, clearCommand.getActionDescription());
    }

}
