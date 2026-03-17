package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_ALIAS = "del";
    public static final int MAX_INDEX_COUNT = 100;

    public static final String MESSAGE_USAGE = COMMAND_WORD 
            + " (Alias:" + COMMAND_ALIAS + "): Deletes the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 3 5 (deletes the 1st, 3rd and 5th person in the displayed list)\n"
            + "Shortcut: " + COMMAND_ALIAS + " 2 (deletes the 2nd person in the displayed list)\n"
            + "Note: Maximum of " + MAX_INDEX_COUNT + " indexes can be provided.";

    /**
     * Returns the usage message for the given command word.
     * This allows the parser to show more contextual errors depending on the alias used.
     */
    public static String getUsage(String commandWord) {
        if (COMMAND_ALIAS.equals(commandWord)) {
            return COMMAND_ALIAS + ": Deletes the person identified by the index number used in the displayed person list.\n"
                    + "Parameters: INDEX (must be a positive integer)\n"
                    + "Example: " + COMMAND_ALIAS + " 1 3 5 (deletes the 1st, 3rd and 5th person in the displayed list)\n"
                    + "Shortcut: " + COMMAND_WORD + " 2 (deletes the 2nd person in the displayed list)\n"
                    + "Note: Maximum of " + MAX_INDEX_COUNT + " indexes can be provided.";
        }
        return MESSAGE_USAGE;
    }

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted employee(s): %1$s";
    private final List<Index> targetIndexes;

    public DeleteCommand(List<Index> targetIndexes) {
        this.targetIndexes = targetIndexes;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        // List of persons to delete
        List<Person> lastShownList = model.getFilteredPersonList();
        List<Person> personsToDelete = new ArrayList<>();

        // Remove duplicate indexes first
        List<Index> uniqueIndexes = targetIndexes.stream().distinct().toList();

        for (Index index : targetIndexes) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personsToDelete.add(lastShownList.get(index.getZeroBased()));
        }

        // Re-sort to delete the highest index first, avoid shifting
        List<Index> sortedIndexes = new ArrayList<>(uniqueIndexes);
        sortedIndexes.sort((a, b) -> Integer.compare(b.getZeroBased(), a.getZeroBased()));

        for (Index index : sortedIndexes) {
            Person personToDelete = lastShownList.get(index.getZeroBased());
            model.deletePerson(personToDelete);
        }

        return new CommandResult(String.format(
                MESSAGE_DELETE_PERSON_SUCCESS,
                personsToDelete.size() + " employee(s)"
        ));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndexes.equals(otherDeleteCommand.targetIndexes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndexes", targetIndexes)
                .toString();
    }
}
