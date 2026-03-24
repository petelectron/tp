package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll; // ADD THIS IMPORT
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javafx.application.Platform; // ADD THIS IMPORT
import seedu.address.logic.Logic;
import seedu.address.logic.LogicManager;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.testutil.TypicalPersons;

public class StatsPanelTest {

    @TempDir
    public Path temporaryFolder;

    private StatsPanel statsPanel;

    @BeforeAll
    public static void initJavaFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // JavaFX is already initialized
        }
    }

    @BeforeEach
    public void setUp() {
        Model model = new ModelManager(TypicalPersons.getTypicalAddressBook(), new UserPrefs());
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        Logic logic = new LogicManager(model, storage);

        statsPanel = new StatsPanel(logic);
    }

    @Test
    public void constructor_nullLogic_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StatsPanel(null));
    }

    @Test
    public void constructor_validLogic_initializesSuccessfully() {
        assertNotNull(statsPanel);
    }
}