package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.util.ToStringBuilder;

/**
 * {@code AddressBook} that keeps track of its previous states for undo/redo functionality.
 */
public class VersionedAddressBook extends AddressBook {

    private static final int MAX_HISTORY_SIZE = 100;

    private final List<ReadOnlyAddressBook> addressBookStateList;
    private int currentStatePointer;

    /**
     * Creates a VersionedAddressBook with an initial state.
     */
    public VersionedAddressBook(ReadOnlyAddressBook initialState) {
        super(initialState);

        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new AddressBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Creates a VersionedAddressBook with an empty initial state.
     */
    public VersionedAddressBook() {
        super();

        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new AddressBook());
        currentStatePointer = 0;
    }

    /**
     * Saves the current address book state in the history.
     */
    public void commit() {
        // Remove all states after the current pointer to discard undone states
        addressBookStateList.subList(currentStatePointer + 1, addressBookStateList.size()).clear();

        // Add the current state as a new state
        addressBookStateList.add(new AddressBook(this));

        // Move the pointer to the new state
        currentStatePointer++;

        // Limit the history size
        if (addressBookStateList.size() > MAX_HISTORY_SIZE) {
            addressBookStateList.remove(0);
            currentStatePointer--;
        }
    }

    /**
     * Restores the previous address book state from history.
     */
    public void undo() {
        requireNonNull(addressBookStateList);
        assert canUndo() : "Should not be able to undo when there are no previous states";

        currentStatePointer--;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Restores a previously undone address book state from history.
     */
    public void redo() {
        requireNonNull(addressBookStateList);
        assert canRedo() : "Should not be able to redo when there are no undone states";

        currentStatePointer++;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has address book states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has address book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < addressBookStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedAddressBook)) {
            return false;
        }

        VersionedAddressBook otherVersionedAddressBook = (VersionedAddressBook) other;

        return super.equals(otherVersionedAddressBook)
                && addressBookStateList.equals(otherVersionedAddressBook.addressBookStateList)
                && currentStatePointer == otherVersionedAddressBook.currentStatePointer;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("addressBook", super.toString())
                .add("currentStatePointer", currentStatePointer)
                .add("addressBookStateList", addressBookStateList)
                .toString();
    }
}

