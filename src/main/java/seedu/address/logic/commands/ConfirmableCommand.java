package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command that requires explicit user confirmation before execution.
 */
public interface ConfirmableCommand {

    /**
     * Returns the prompt shown to the user when confirmation is required.
     */
    String getConfirmationPrompt();

    /**
     * Returns the prompt shown to the user when confirmation is required, with model context.
     *
     * <p>Commands that need model state (e.g. target names from indices) can override this.
     */
    default String getConfirmationPrompt(Model model) {
        return getConfirmationPrompt();
    }

    /**
     * Validates preconditions against the current model state before the confirmation prompt is shown.
     *
     * <p>Commands that need pre-confirmation validation (e.g. index range checks) should override this.
     * Throwing here prevents a misleading confirmation prompt from being shown to the user.
     *
     * @throws CommandException if the command cannot proceed
     */
    default void validateBeforeConfirm(Model model) throws CommandException {
        // no-op by default
    }

    /**
     * Returns a short action description used in cancellation feedback.
     */
    String getActionDescription();
}
