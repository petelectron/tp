package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;

import org.junit.jupiter.api.Test;

public class VersionedAddressBookTest {

    private final VersionedAddressBook versionedAddressBook = new VersionedAddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), versionedAddressBook.getPersonList());
        assertFalse(versionedAddressBook.canUndo());
        assertFalse(versionedAddressBook.canRedo());
    }

    @Test
    public void constructor_withInitialState() {
        AddressBook initialState = getTypicalAddressBook();
        VersionedAddressBook versionedBook = new VersionedAddressBook(initialState);
        assertEquals(initialState, versionedBook);
        assertFalse(versionedBook.canUndo());
        assertFalse(versionedBook.canRedo());
    }

    @Test
    public void commit_addsStateToHistory() {
        versionedAddressBook.addPerson(ALICE);
        versionedAddressBook.commit();
        assertTrue(versionedAddressBook.canUndo());
        assertFalse(versionedAddressBook.canRedo());
    }

    @Test
    public void undo_restoresPreviousState() {
        versionedAddressBook.addPerson(ALICE);
        versionedAddressBook.commit();
        versionedAddressBook.addPerson(BOB);
        versionedAddressBook.commit();

        assertTrue(versionedAddressBook.canUndo());
        versionedAddressBook.undo();
        assertTrue(versionedAddressBook.canUndo());
        assertTrue(versionedAddressBook.canRedo());
        assertTrue(versionedAddressBook.hasPerson(ALICE));
        assertFalse(versionedAddressBook.hasPerson(BOB));
    }

    @Test
    public void redo_restoresUndoneState() {
        versionedAddressBook.addPerson(ALICE);
        versionedAddressBook.commit();
        versionedAddressBook.addPerson(BOB);
        versionedAddressBook.commit();

        versionedAddressBook.undo();
        assertTrue(versionedAddressBook.canRedo());
        versionedAddressBook.redo();
        assertTrue(versionedAddressBook.canUndo());
        assertFalse(versionedAddressBook.canRedo());
        assertTrue(versionedAddressBook.hasPerson(ALICE));
        assertTrue(versionedAddressBook.hasPerson(BOB));
    }

    @Test
    public void commit_afterUndo_discardsFutureStates() {
        versionedAddressBook.addPerson(ALICE);
        versionedAddressBook.commit();
        versionedAddressBook.addPerson(BOB);
        versionedAddressBook.commit();

        versionedAddressBook.undo();
        // After undo, we have ALICE but not BOB
        assertTrue(versionedAddressBook.hasPerson(ALICE));
        assertFalse(versionedAddressBook.hasPerson(BOB));

        // Now add BOB again and commit - this should discard the ability to redo to the previous BOB state
        versionedAddressBook.addPerson(BOB);
        versionedAddressBook.commit();

        assertTrue(versionedAddressBook.canUndo());
        assertFalse(versionedAddressBook.canRedo()); // Should not be able to redo to the previous BOB state
    }

    @Test
    public void canUndo_initialState_returnsFalse() {
        assertFalse(versionedAddressBook.canUndo());
    }

    @Test
    public void canRedo_initialState_returnsFalse() {
        assertFalse(versionedAddressBook.canRedo());
    }

    @Test
    public void equals() {
        AddressBook initialState = getTypicalAddressBook();
        VersionedAddressBook versionedBook1 = new VersionedAddressBook(initialState);
        VersionedAddressBook versionedBook2 = new VersionedAddressBook(initialState);

        // Same initial state
        assertTrue(versionedBook1.equals(versionedBook2));

        // After commit
        versionedBook1.commit();
        assertFalse(versionedBook1.equals(versionedBook2));
        versionedBook2.commit();
        assertTrue(versionedBook1.equals(versionedBook2));
    }
}

