package seedu.address.logic;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Statistics;
import seedu.address.model.StatisticsMode;
import seedu.address.model.person.Person;

/**
 * Service class that provides statistics about employees.
 * This follows the Service layer pattern to keep business logic separate from UI.
 *
 * <p>The StatisticsService acts as a bridge between the UI components that display
 * statistics and the underlying data model. It retrieves employee data from the
 * {@link Logic} component and creates {@link Statistics} objects for display.
 */
public class StatisticsService {

    private static final Logger logger = LogsCenter.getLogger(StatisticsService.class);
    private final Logic logic;

    /**
     * Creates a StatisticsService with the given Logic component.
     *
     * @param logic The Logic component used to access employee data. Must not be null.
     * @throws NullPointerException if logic is null.
     */
    public StatisticsService(Logic logic) {
        requireNonNull(logic);
        this.logic = logic;
        logger.info("StatisticsService initialized");
    }

    /**
     * Returns current statistics based on the full HRmanager.
     *
     * <p>This method uses the default statistics mode, which is {@link StatisticsMode#DEPARTMENT}.
     *
     * @return A {@link Statistics} object containing metrics computed from all employees.
     */
    public Statistics getCurrentStatistics() {
        return getCurrentStatistics(StatisticsMode.DEPARTMENT);
    }

    /**
     * Returns current statistics based on the full HRmanager and selected dashboard mode.
     *
     * <p>The statistics are computed from all employees in the HRmanager,
     * regardless of any active filters (e.g., search results).
     *
     * @param statisticsMode The mode to calculate statistics for (TAG, DEPARTMENT, or ROLE). Must not be null.
     * @return A {@link Statistics} object containing metrics computed from all employees
     *         using the specified mode.
     * @throws NullPointerException if statisticsMode is null.
     */
    public Statistics getCurrentStatistics(StatisticsMode statisticsMode) {
        requireNonNull(statisticsMode);
        logger.fine("Getting current statistics");
        List<Person> persons = logic.getAddressBook().getPersonList();
        return new Statistics(persons, statisticsMode);
    }
}
