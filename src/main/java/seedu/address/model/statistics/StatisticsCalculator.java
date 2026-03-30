package seedu.address.model.statistics;

import java.util.List;

import seedu.address.model.person.Person;

/**
 * Contract for a mode-specific statistics calculator.
 */
public interface StatisticsCalculator {

    /**
     * Computes statistics for the provided list of persons.
     */
    StatisticsComputation compute(List<Person> persons);
}

