package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Undoes the previous command that modified the HRmanager.
 */
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Undoes the previous command that modified the HRmanager.\n"
        + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Undid the previous command.";
    public static final String MESSAGE_FAILURE = "There are no commands to undo.";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (!model.canUndoAddressBook()) {
            throw new CommandException(MESSAGE_FAILURE);
        }

        model.isUndoAddressBookSuccessful();
        return new CommandResult(MESSAGE_SUCCESS);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof UndoCommand;
    }
}

