package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.ExitCommand.MESSAGE_EXIT_ACKNOWLEDGEMENT;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;

public class ExitCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_exit_success() {
        CommandResult expectedCommandResult = new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
        assertCommandSuccess(new ExitCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void getConfirmationPrompt_returnsExpectedPrompt() {
        ExitCommand exitCommand = new ExitCommand();
        assertEquals(
            ConfirmationPromptFormatter.format(
                ExitCommand.ACTION_SUMMARY,
                ExitCommand.IMPACT_SUMMARY),
            exitCommand.getConfirmationPrompt());
    }

    @Test
    public void getActionDescription_returnsExpectedDescription() {
        ExitCommand exitCommand = new ExitCommand();
        assertEquals(ExitCommand.ACTION_DESCRIPTION, exitCommand.getActionDescription());
    }
}
