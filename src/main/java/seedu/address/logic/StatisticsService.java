package seedu.address.logic;

import java.util.List;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.Statistics;
import seedu.address.model.person.Person;

import static java.util.Objects.requireNonNull;

/**
 * Service class that provides statistics about employees.
 * This follows the Service layer pattern to keep business logic separate from UI.
 */
public class StatisticsService {

    private static final Logger logger = LogsCenter.getLogger(StatisticsService.class);
    private final Logic logic;

    public StatisticsService(Logic logic) {
        requireNonNull(logic);
        this.logic = logic;
        logger.info("StatisticsService initialized");
    }

    /**
     * Returns current statistics based on the filtered person list.
     */
    public Statistics getCurrentStatistics() {
        logger.fine("Getting current statistics");
        ObservableList<Person> observableList = logic.getFilteredPersonList();
        // ObservableList is a List, so this works fine
        List<Person> personList = observableList;
        return new Statistics(personList);
    }
}