package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;


public class ImportCommandTest {

    private static final String VALID_HEADER = "name,phone,email,role,department";
    private static final String VALID_ROW_1 = "Alice Tan,91234567,alice@example.com,Software Engineer,Backend";
    private static final String VALID_ROW_2 = "Bob Lee,98765432,bob@example.com,UI Designer,Design";

    @TempDir
    Path tempDir;

    private ModelStub model;

    private static class ModelStub implements Model {

        private AddressBook addressBook = new AddressBook();
        private AddressBook lastSetAddressBook = null;
        private boolean commitCalled = false;

        public boolean isCommitCalled() {
            return commitCalled;
        }

        public AddressBook getLastSetAddressBook() {
            return lastSetAddressBook;
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            this.commitCalled = true;
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
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }

        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("unexpected call");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("unexpected call");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("unexpected call");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("unexpected call");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("unexpected call");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("unexpected call");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook addressBook) {
            this.lastSetAddressBook = new AddressBook();
            addressBook.getPersonList().forEach(lastSetAddressBook::addPerson);
            this.addressBook = lastSetAddressBook;
        }

        @Override
        public void addPerson(Person p) {
            throw new AssertionError("unexpected call");
        }

        @Override
        public boolean hasPerson(Person p) {
            throw new AssertionError("unexpected call");
        }

        @Override
        public void deletePerson(Person p) {
            throw new AssertionError("unexpected call");
        }

        @Override
        public void setPerson(Person t, Person e) {
            throw new AssertionError("unexpected call");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("unexpected call");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("unexpected call");
        }
    }

    @BeforeEach
    void setUp() {
        model = new ModelStub();
    }

    @Test
    void execute_validCsvMultiplePeople_allPersonsImported() throws Exception {
        Path csv = writeCsv(VALID_HEADER, VALID_ROW_1, VALID_ROW_2);

        ImportCommand cmd = new ImportCommand(csv.toString());
        cmd.execute(model);

        assertEquals(2, model.lastSetAddressBook.getPersonList().size());
        assertTrue(model.isCommitCalled());
    }

    @Test
    void execute_csvWithHeaderOnly_setsEmptyAddressBookAndReturnsEmptyMessage() throws Exception {
        Path csv = writeCsv(VALID_HEADER); // data rows only — header-only file

        CommandResult result = new ImportCommand(csv.toString()).execute(model);

        assertNotNull(model.lastSetAddressBook);
        assertEquals(0, model.lastSetAddressBook.getPersonList().size());
        assertEquals(ImportCommand.MESSAGE_EMPTY_FILE, result.getFeedbackToUser());
    }

    @Test
    void execute_malformedCsvMissingRequiredColumn_throwsCommandExceptionWithParseMessage() throws Exception {
        // 'phone' column is absent
        Path csv = writeCsv("name,email,address", "Alice,alice@example.com,123 Main St");
        ImportCommand cmd = new ImportCommand(csv.toString());

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertTrue(ex.getMessage().startsWith("Failed to parse CSV file"));
        // Model must NOT have been modified
        assertNull(model.lastSetAddressBook);
    }

    @Test
    void execute_emptyCsvFile_throwsCommandExceptionWithParseMessage() throws Exception {
        Path csv = writeRaw("");
        ImportCommand cmd = new ImportCommand(csv.toString());

        CommandException ex = assertThrows(CommandException.class, () -> cmd.execute(model));
        assertTrue(ex.getMessage().startsWith("Failed to parse CSV file"));
        assertNull(model.lastSetAddressBook);
    }

    @Test
    void equals_samePath_returnsTrue() {
        assertTrue(new ImportCommand("/a/b.csv").equals(new ImportCommand("/a/b.csv")));
    }

    @Test
    void equals_differentPath_returnsFalse() {
        assertNotEquals(new ImportCommand("/a/b.csv"), new ImportCommand("/x/y.csv"));
    }

    @Test
    void equals_sameObject_returnsTrue() {
        ImportCommand cmd = new ImportCommand("/a/b.csv");
        assertEquals(cmd, cmd);
    }

    @Test
    void equals_differentType_returnsFalse() {
        assertNotEquals(new ImportCommand("/a/b.csv"), "not a command");
    }

    @Test
    void getConfirmationPrompt_containsActionAndImpactSummary() {
        ImportCommand cmd = new ImportCommand("/some/path.csv");
        String prompt = cmd.getConfirmationPrompt();
        assertTrue(prompt.contains(ImportCommand.ACTION_SUMMARY));
        assertTrue(prompt.contains(ImportCommand.IMPACT_SUMMARY));
    }

    @Test
    void getActionDescription_returnsExpectedString() {
        assertEquals(ImportCommand.ACTION_DESCRIPTION,
            new ImportCommand("/some/path.csv").getActionDescription());
    }

    private Path writeCsv(String... lines) {
        return writeRaw(String.join("\n", lines));
    }

    private Path writeRaw(String content) {
        try {
            Path file = tempDir.resolve("test.csv");
            Files.writeString(file, content);
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
