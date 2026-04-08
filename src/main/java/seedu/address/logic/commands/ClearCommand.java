package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;

/**
 * Clears the HRmanager.
 */
public class ClearCommand extends Command implements ConfirmableCommand {

    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_SUCCESS = "Employee list has been cleared!";
    public static final String ACTION_SUMMARY = "Clear all employees.";
    public static final String IMPACT_SUMMARY =
            "All employee records will be permanently removed.";
    public static final String ACTION_DESCRIPTION = "clear all employees";

    /**
     * Returns the confirmation prompt for this clear command.
     *
     * @return Confirmation prompt shown before execution.
     */
    @Override
    public String getConfirmationPrompt() {
        return ConfirmationPromptFormatter.format(ACTION_SUMMARY, IMPACT_SUMMARY);
    }

    /**
     * Returns a short description of the command action.
     *
     * @return Action description used in cancellation feedback.
     */
    @Override
    public String getActionDescription() {
        return ACTION_DESCRIPTION;
    }


    /**
     * Clears all employees from the address book.
     *
     * @param model Model on which the command operates.
     * @return Command result indicating clear success.
     */
    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        AddressBook currentAddressBook = new AddressBook(model.getAddressBook());
        AddressBook clearedAddressBook = new AddressBook();
        model.setAddressBook(clearedAddressBook);
        if (!currentAddressBook.equals(clearedAddressBook)) {
            model.commitAddressBook();
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
