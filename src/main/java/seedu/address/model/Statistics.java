package seedu.address.model;

import java.util.List;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.statistics.DepartmentStatisticsCalculator;
import seedu.address.model.statistics.StatisticsCalculator;
import seedu.address.model.statistics.StatisticsComputation;
import seedu.address.model.statistics.TagStatisticsCalculator;

/**
 * Represents statistics calculated from employee records.
 * This is a data container with no UI logic.
 */
public class Statistics {

    private static final Logger logger = LogsCenter.getLogger(Statistics.class);

    private final StatisticsMode statisticsMode;
    private final int totalEmployees;
    private final int uniqueValueCount;
    private final String mostCommonValue;
    private final int employeesWithValue;
    private final int employeesWithoutValue;
    private final String valueDistribution;

    /**
     * Creates a Statistics object by calculating from a list of persons in tag mode.
     *
     * @param persons The list of persons to calculate statistics from
     */
    public Statistics(List<Person> persons) {
        this(persons, StatisticsMode.TAG);
    }

    /**
     * Creates a Statistics object by calculating from a list of persons with the selected mode.
     *
     * @param persons The list of persons to calculate statistics from
     * @param statisticsMode The mode to calculate statistics for
     */
    public Statistics(List<Person> persons, StatisticsMode statisticsMode) {
        assert persons != null : "Person list cannot be null";
        assert statisticsMode != null : "Statistics mode cannot be null";

        this.statisticsMode = statisticsMode;
        this.totalEmployees = persons.size();
        logger.fine("Calculating " + statisticsMode.getFullName() + " statistics for "
                + totalEmployees + " employees");

        StatisticsComputation computation = getCalculatorForMode(statisticsMode).compute(persons);
        this.employeesWithValue = computation.getEmployeesWithValue();
        this.employeesWithoutValue = computation.getEmployeesWithoutValue();
        this.uniqueValueCount = computation.getUniqueValueCount();
        this.mostCommonValue = computation.getMostCommonValue();
        this.valueDistribution = computation.getDistribution();

        logger.fine("Statistics calculated in " + statisticsMode.getFullName() + " mode: "
                + uniqueValueCount + " unique values, " + employeesWithValue + " employees with values");
    }

    private StatisticsCalculator getCalculatorForMode(StatisticsMode mode) {
        return switch (mode) {
        case TAG -> new TagStatisticsCalculator();
        case DEPARTMENT -> new DepartmentStatisticsCalculator();
        };
    }

    public StatisticsMode getStatisticsMode() {
        return statisticsMode;
    }

    // Existing getters are preserved for compatibility with current UI and tests.
    public int getTotalEmployees() {
        return totalEmployees;
    }

    public int getUniqueValueCount() {
        return uniqueValueCount;
    }

    public String getMostCommonValue() {
        return mostCommonValue;
    }

    public int getEmployeesWithValue() {
        return employeesWithValue;
    }

    public int getEmployeesWithoutValue() {
        return employeesWithoutValue;
    }

    public String getValueDistribution() {
        return valueDistribution;
    }
}
