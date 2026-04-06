package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.VersionedAddressBook;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class UndoCommandTest {

    @Test
    public void execute_undoSuccessful_success() throws Exception {
        ModelStubWithUndo modelStub = new ModelStubWithUndo();
        modelStub.commitAddressBook(); // Simulate a previous command

        CommandResult commandResult = new UndoCommand().execute(modelStub);

        assertEquals(UndoCommand.MESSAGE_SUCCESS, commandResult.getFeedbackToUser());
        assertTrue(modelStub.isUndoCalled());
    }

    @Test
    public void execute_nothingToUndo_throwsCommandException() {
        ModelStubWithUndo modelStub = new ModelStubWithUndo();

        assertThrows(CommandException.class,
                UndoCommand.MESSAGE_FAILURE, () -> new UndoCommand().execute(modelStub));
    }

    @Test
    public void equals() {
        UndoCommand undoCommand1 = new UndoCommand();
        UndoCommand undoCommand2 = new UndoCommand();

        assertTrue(undoCommand1.equals(undoCommand2));
        assertTrue(undoCommand1.equals(undoCommand1));
        assertFalse(undoCommand1.equals(null));
        assertFalse(undoCommand1.equals(new AddCommand(new PersonBuilder().build())));
    }

    /**
     * A Model stub that supports undo operations.
     */
    private class ModelStubWithUndo extends ModelStub {
        private boolean undoCalled = false;
        private final VersionedAddressBook addressBook = new VersionedAddressBook();

        public boolean isUndoCalled() {
            return undoCalled;
        }

        @Override
        public void commitAddressBook() {
            addressBook.commit();
        }

        @Override
        public boolean undoAddressBook() {
            if (canUndoAddressBook()) {
                addressBook.undo();
                undoCalled = true;
                return true;
            }
            return false;
        }

        @Override
        public boolean canUndoAddressBook() {
            return addressBook.canUndo();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }
    }

    /**
     * A default Model stub.
     */
    private static class ModelStub implements Model {

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean undoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean redoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }
    }
}

