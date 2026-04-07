package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    public static final int MAX_INDEX_COUNT = 10;
    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted employee(s): %1$s";
    public static final String ACTION_SUMMARY_FORMAT = "Delete %1$d employee(s).";
    public static final String IMPACT_SUMMARY = "The selected employee record(s) will be permanently removed.";
    public static final String IMPACT_SUMMARY_WITH_NAMES_FORMAT =
            "The selected employee record(s) will be permanently removed: %1$s.";
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
    public void validateBeforeConfirm(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        for (Index index : getUniqueIndexes()) {
            if (index.getZeroBased() >= lastShownList.size()) {
                logger.fine("Pre-confirmation validation failed for out-of-range index: " + index.getOneBased());
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }
    }

    @Override
    public String getConfirmationPrompt() {
        int uniqueCount = getUniqueIndexes().size();
        assert uniqueCount > 0 : "Delete confirmation should refer to at least one unique index";
        assert uniqueCount <= targetIndexes.size() : "Unique index count cannot exceed requested count";

        String actionSummary = String.format(ACTION_SUMMARY_FORMAT, uniqueCount);

        return ConfirmationPromptFormatter.format(actionSummary, IMPACT_SUMMARY);
    }

    @Override
    public String getConfirmationPrompt(Model model) {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        List<String> targetLabels = getUniqueIndexes().stream()
                .sorted((a, b) -> Integer.compare(a.getZeroBased(), b.getZeroBased()))
                .map(index -> lastShownList.get(index.getZeroBased()).getName().fullName)
                .collect(Collectors.toList());

        String actionSummary = String.format(ACTION_SUMMARY_FORMAT, targetLabels.size());
        String impactSummary = String.format(IMPACT_SUMMARY_WITH_NAMES_FORMAT, String.join(", ", targetLabels));
        return ConfirmationPromptFormatter.format(actionSummary, impactSummary);
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

        List<Person> lastShownList = getLastShownList(model);
        int initialListSize = lastShownList.size();

        // Remove duplicate indexes first
        List<Index> uniqueIndexes = getUniqueIndexes();
        assert !uniqueIndexes.isEmpty() : "DeleteCommand should retain at least one unique index";
        logger.finer("Delete deduplicated indexes from " + targetIndexes.size()
                + " to " + uniqueIndexes.size());

        validateUniqueIndexesWithinBounds(uniqueIndexes, lastShownList);

        List<String> deletedNamesInDisplayOrder = collectDeletedNamesInDisplayOrder(uniqueIndexes, lastShownList);

        List<Index> sortedIndexes = sortIndexesDescending(uniqueIndexes);
        deletePersonsAtIndexes(sortedIndexes, lastShownList, model);

        int finalListSize = model.getFilteredPersonList().size();
        assertDeletedCountMatches(initialListSize, finalListSize, uniqueIndexes.size());

        logger.fine("Deleted " + uniqueIndexes.size() + " unique employee(s) successfully");

        model.commitAddressBook();

        return new CommandResult(String.format(
                MESSAGE_DELETE_PERSON_SUCCESS,
                String.join(", ", deletedNamesInDisplayOrder)
        ));
    }

    private List<Person> getLastShownList(Model model) {
        List<Person> lastShownList = model.getFilteredPersonList();
        assert lastShownList != null : "Filtered person list should not be null";
        return lastShownList;
    }

    private void validateUniqueIndexesWithinBounds(List<Index> uniqueIndexes, List<Person> lastShownList)
            throws CommandException {
        for (Index index : uniqueIndexes) {
            assert index != null : "DeleteCommand encountered a null index";
            if (index.getZeroBased() >= lastShownList.size()) {
                logger.fine("Delete failed validation for out-of-range index: " + index.getOneBased());
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
        }
    }

    private List<String> collectDeletedNamesInDisplayOrder(List<Index> uniqueIndexes, List<Person> lastShownList) {
        List<String> deletedNamesInDisplayOrder = new ArrayList<>();
        for (Index index : uniqueIndexes.stream()
                .sorted((a, b) -> Integer.compare(a.getZeroBased(), b.getZeroBased()))
                .toList()) {
            deletedNamesInDisplayOrder.add(lastShownList.get(index.getZeroBased()).getName().fullName);
        }
        return deletedNamesInDisplayOrder;
    }

    private List<Index> sortIndexesDescending(List<Index> uniqueIndexes) {
        List<Index> sortedIndexes = new ArrayList<>(uniqueIndexes);
        sortedIndexes.sort((a, b) -> Integer.compare(b.getZeroBased(), a.getZeroBased()));
        assert isSortedInDescendingOrder(sortedIndexes) : "Delete indexes should be sorted descending";
        return sortedIndexes;
    }

    private void deletePersonsAtIndexes(List<Index> sortedIndexes, List<Person> lastShownList, Model model) {
        for (Index index : sortedIndexes) {
            Person personToDelete = lastShownList.get(index.getZeroBased());
            assert personToDelete != null : "Person selected for deletion should not be null";
            model.deletePerson(personToDelete);
        }
    }

    private void assertDeletedCountMatches(int initialListSize, int finalListSize, int expectedDeletedCount) {
        assert initialListSize - finalListSize == expectedDeletedCount
                : "Deleted person count does not match the number of unique indexes";
    }

    private List<Index> getUniqueIndexes() {
        return targetIndexes.stream().distinct().toList();
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
