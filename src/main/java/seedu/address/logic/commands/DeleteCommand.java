package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command implements ConfirmableCommand {

    public static final String COMMAND_WORD = "delete";
    public static final String COMMAND_ALIAS = "del";
    public static final int MAX_INDEX_COUNT = 100;
    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted employee(s): %1$s";
    public static final String ACTION_SUMMARY_FORMAT = "Delete %1$d employee(s).";
    public static final String IMPACT_SUMMARY =
            "The selected employee record(s) will be permanently removed.";
    public static final String ACTION_DESCRIPTION = "delete employee(s)";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + " (Alias:" + COMMAND_ALIAS + "): Deletes the person identified "
            + "by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1 3 5 (deletes the 1st,"
            + "3rd and 5th person in the displayed list)\n"
            + "Shortcut: " + COMMAND_ALIAS + " 2 (deletes the "
            + "2nd person in the displayed list)\n"
            + "Note: Maximum of " + MAX_INDEX_COUNT + " indexes can be provided.";

    private static final Logger logger = LogsCenter.getLogger(DeleteCommand.class);

    private final List<Index> targetIndexes;

    /**
     * Creates a DeleteCommand for the provided displayed indexes.
     */
    public DeleteCommand(List<Index> targetIndexes) {
        requireNonNull(targetIndexes);
        targetIndexes.forEach(java.util.Objects::requireNonNull);
        assert !targetIndexes.isEmpty() : "DeleteCommand requires at least one target index";
        assert targetIndexes.size() <= MAX_INDEX_COUNT : "DeleteCommand received too many indexes";

        this.targetIndexes = targetIndexes;
        logger.fine("DeleteCommand created with " + targetIndexes.size() + " requested index(es)");
    }

    @Override
    public String getConfirmationPrompt() {
        int uniqueCount = (int) targetIndexes.stream().distinct().count();
        assert uniqueCount > 0 : "Delete confirmation should refer to at least one unique index";
        assert uniqueCount <= targetIndexes.size() : "Unique index count cannot exceed requested count";

        String actionSummary = String.format(ACTION_SUMMARY_FORMAT, uniqueCount);

        return ConfirmationPromptFormatter.format(actionSummary, IMPACT_SUMMARY);
    }

    @Override
    public String getActionDescription() {
        return ACTION_DESCRIPTION;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        assert !targetIndexes.isEmpty() : "DeleteCommand should not execute with no indexes";

        logger.fine("Executing delete for " + targetIndexes.size() + " requested index(es)");

        // List of persons to delete
        List<Person> lastShownList = model.getFilteredPersonList();
        assert lastShownList != null : "Filtered person list should not be null";
        int initialListSize = lastShownList.size();

        // Remove duplicate indexes first
        List<Index> uniqueIndexes = targetIndexes.stream().distinct().toList();
        assert !uniqueIndexes.isEmpty() : "DeleteCommand should retain at least one unique index";
        logger.finer("Delete deduplicated indexes from " + targetIndexes.size()
                + " to " + uniqueIndexes.size());

        // Validate all indexes before deleting
        for (Index index : uniqueIndexes) {
            assert index != null : "DeleteCommand encountered a null index";
            if (index.getZeroBased() >= lastShownList.size()) {
                logger.fine("Delete failed validation for out-of-range index: " + index.getOneBased());
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }

        // Re-sort to delete the highest index first, avoid shifting
        List<Index> sortedIndexes = new ArrayList<>(uniqueIndexes);
        sortedIndexes.sort((a, b) -> Integer.compare(b.getZeroBased(), a.getZeroBased()));
        assert isSortedInDescendingOrder(sortedIndexes) : "Delete indexes should be sorted descending";

        for (Index index : sortedIndexes) {
            Person personToDelete = lastShownList.get(index.getZeroBased());
            assert personToDelete != null : "Person selected for deletion should not be null";
            model.deletePerson(personToDelete);
        }

        int finalListSize = model.getFilteredPersonList().size();
        assert initialListSize - finalListSize == uniqueIndexes.size()
                : "Deleted person count does not match the number of unique indexes";

        logger.fine("Deleted " + uniqueIndexes.size() + " unique employee(s) successfully");

        model.commitAddressBook();

        return new CommandResult(String.format(
                MESSAGE_DELETE_PERSON_SUCCESS,
                uniqueIndexes.size() + " employee(s)"
        ));
    }

    /**
     * Returns true if the provided indexes are sorted in descending zero-based order.
     */
    private boolean isSortedInDescendingOrder(List<Index> indexes) {
        for (int i = 1; i < indexes.size(); i++) {
            if (indexes.get(i - 1).getZeroBased() < indexes.get(i).getZeroBased()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand otherDeleteCommand)) {
            return false;
        }
        return targetIndexes.equals(otherDeleteCommand.targetIndexes);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndexes", targetIndexes)
                .toString();
    }
}
