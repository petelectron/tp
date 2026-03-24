package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.Statistics;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;

public class StatisticsServiceTest {

    @TempDir
    public Path temporaryFolder;

    private Model model;
    private Logic logic;
    private StatisticsService statisticsService;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        JsonAddressBookStorage addressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("addressBook.json"));
        JsonUserPrefsStorage userPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("userPrefs.json"));
        StorageManager storage = new StorageManager(addressBookStorage, userPrefsStorage);
        logic = new LogicManager(model, storage);
        statisticsService = new StatisticsService(logic);
    }

    @Test
    public void constructor_nullLogic_throwsNullPointerException() {
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () ->
                new StatisticsService(null));
    }

    @Test
    public void getCurrentStatistics_validLogic_returnsStatistics() {
        Statistics stats = statisticsService.getCurrentStatistics();

        assertNotNull(stats);
        assertEquals(model.getFilteredPersonList().size(), stats.getTotalEmployees());
    }

    @Test
    public void getCurrentStatistics_emptyList_returnsEmptyStats() {
        StatisticsService emptyStatisticsService = createEmptyStatisticsService();
        Statistics stats = emptyStatisticsService.getCurrentStatistics();

        assertEquals(0, stats.getTotalEmployees());
        assertEquals("None", stats.getMostCommonTag());
        assertEquals("No tags yet", stats.getTagDistribution());
    }

    @Test
    public void getCurrentStatistics_afterAddingEmployee_updatesCorrectly() throws Exception {
        // Initial stats
        Statistics initialStats = statisticsService.getCurrentStatistics();
        int initialCount = initialStats.getTotalEmployees();
        int initialUniqueTags = initialStats.getUniqueTagCount(); // Store initial tag count

        // Add a new employee with a new tag
        String addCommand = "add n/Test User p/12345678 e/test@example.com r/Employee t/TestTag";
        logic.execute(addCommand);

        // Verify stats updated
        Statistics updatedStats = statisticsService.getCurrentStatistics();
        assertEquals(initialCount + 1, updatedStats.getTotalEmployees());
        // The unique tag count should increase by 1 (since TestTag is new)
        assertEquals(initialUniqueTags + 1, updatedStats.getUniqueTagCount());
        // The most common tag might not be TestTag if other tags are more common
        // So either remove this assertion or make it more flexible
        // assertEquals("TestTag (1)", updatedStats.getMostCommonTag());
    }

    @Test
    public void getCurrentStatistics_afterDeletingEmployee_updatesCorrectly() throws Exception {
        // Add an employee first
        String addCommand = "add n/ToDelete p/87654321 e/todelete@example.com r/Employee t/DeleteTag";
        logic.execute(addCommand);

        // Verify employee was added
        Statistics statsAfterAdd = statisticsService.getCurrentStatistics();
        int countAfterAdd = statsAfterAdd.getTotalEmployees();

        // Delete the employee (assumes it's the last one)
        String deleteCommand = "delete " + countAfterAdd;
        logic.execute(deleteCommand);

        // Verify stats updated
        Statistics statsAfterDelete = statisticsService.getCurrentStatistics();
        assertEquals(countAfterAdd - 1, statsAfterDelete.getTotalEmployees());
    }

    @Test
    public void getCurrentStatistics_logging_enabled() {
        // Smoke test to verify logging works
        Statistics stats = statisticsService.getCurrentStatistics();
        assertNotNull(stats);
        // Logging output can be seen in console when running tests with logging enabled
    }

    /**
     * Creates a StatisticsService with an empty model.
     */
    private StatisticsService createEmptyStatisticsService() {
        Model emptyModel = new ModelManager();
        JsonAddressBookStorage emptyAddressBookStorage =
                new JsonAddressBookStorage(temporaryFolder.resolve("emptyAddressBook.json"));
        JsonUserPrefsStorage emptyUserPrefsStorage =
                new JsonUserPrefsStorage(temporaryFolder.resolve("emptyUserPrefs.json"));
        StorageManager emptyStorage = new StorageManager(emptyAddressBookStorage, emptyUserPrefsStorage);
        Logic emptyLogic = new LogicManager(emptyModel, emptyStorage);
        return new StatisticsService(emptyLogic);
    }
}
