package seedu.address.model;

import java.nio.file.Path;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Person;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluate to true */
    Predicate<Person> PREDICATE_SHOW_ALL_PERSONS = unused -> true;

    /**
     * Replaces user prefs data with the data in {@code userPrefs}.
     */
    void setUserPrefs(ReadOnlyUserPrefs userPrefs);

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Returns the user prefs' HRmanager file path.
     */
    Path getAddressBookFilePath();

    /**
     * Sets the user prefs' HRmanager file path.
     */
    void setAddressBookFilePath(Path addressBookFilePath);

    /**
     * Replaces HRmanager data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * Returns true if a person with the same identity as {@code person} exists in the HRmanager.
     */
    boolean hasPerson(Person person);

    /**
     * Deletes the given person.
     * The person must exist in the HRmanager.
     */
    void deletePerson(Person target);

    /**
     * Adds the given person.
     * {@code person} must not already exist in the HRmanager.
     */
    void addPerson(Person person);

    /**
     * Replaces the given person {@code target} with {@code editedPerson}.
     * {@code target} must exist in the HRmanager.
     * The person identity of {@code editedPerson} must not be the same as another existing person in the HRmanager.
     */
    void setPerson(Person target, Person editedPerson);

    /** Returns an unmodifiable view of the filtered person list */
    ObservableList<Person> getFilteredPersonList();

    /**
     * Updates the filter of the filtered person list to filter by the given {@code predicate}.
     * @throws NullPointerException if {@code predicate} is null.
     */
    void updateFilteredPersonList(Predicate<Person> predicate);

    /**
     * Saves the current HRmanager state in history.
     */
    void commitAddressBook();

    /**
     * Restores the previous HRmanager state from history.
     * @return true if undo was successful, false if there are no states to undo.
     */
    boolean isUndoAddressBookSuccessful();

    /**
     * Restores a previously undone HRmanager state from history.
     * @return true if redo was successful, false if there are no states to redo.
     */
    boolean isRedoAddressBookSuccessful();

    /**
     * Returns true if there are HRmanager states to undo.
     */
    boolean canUndoAddressBook();

    /**
     * Returns true if there are HRmanager states to redo.
     */
    boolean canRedoAddressBook();
}
