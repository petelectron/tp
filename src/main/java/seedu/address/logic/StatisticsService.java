package seedu.address.logic;

import java.util.List;

import javafx.collections.ObservableList;
import seedu.address.model.Statistics;
import seedu.address.model.person.Person;

/**
 * Service class that provides statistics about employees.
 * This follows the Service layer pattern to keep business logic separate from UI.
 */
public class StatisticsService {

    private final Logic logic;

    public StatisticsService(Logic logic) {
        this.logic = logic;
    }

    /**
     * Returns current statistics based on the filtered person list.
     */
    public Statistics getCurrentStatistics() {
        ObservableList<Person> observableList = logic.getFilteredPersonList();
        // ObservableList is a List, so this works fine
        List<Person> personList = observableList;
        return new Statistics(personList);
    }
}