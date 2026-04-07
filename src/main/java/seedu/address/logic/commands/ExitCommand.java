package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command implements ConfirmableCommand {

    public static final String COMMAND_WORD = "exit";

    public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting HRmanager as requested ..."; // Note that:
    // users effectively NEVER see this message, but is kept as a constant for consistency with other Commands.
    public static final String IMPACT_SUMMARY = "After closing the app,"
        + " past command history will be cleared. Data will be saved.";
    public static final String ACTION_DESCRIPTION = "Exit and close the HRmanager application.";
    public static final String ACTION_SUMMARY = ACTION_DESCRIPTION; // for consistency with other Commands

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT, false, true);
    }

    @Override
    public String getConfirmationPrompt() {
        return ConfirmationPromptFormatter.format(ACTION_SUMMARY, IMPACT_SUMMARY);
    }

    @Override
    public String getActionDescription() {
        return ACTION_DESCRIPTION;
    }

}
