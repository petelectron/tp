package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class StatisticsServiceIntegrationTest {

    @TempDir
    public Path temporaryFolder;

    private Logic logic;

    @BeforeEach
    public void setUp() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("HRmanager.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
    }

    @Test
    public void statisticsService_initializesWithoutError() {
        // Just verify the service can be created without exceptions
        StatisticsService service = new StatisticsService(logic);
        assertNotNull(service);
        assertNotNull(service.getCurrentStatistics());
    }
}
