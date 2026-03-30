package seedu.address.model.statistics;

/**
 * Immutable result of a statistics calculation pass.
 */
public class StatisticsComputation {

    private final int uniqueValueCount;
    private final String mostCommonValue;
    private final int employeesWithValue;
    private final int employeesWithoutValue;
    private final String distribution;

    /**
     * Creates a computed statistics result.
     */
    public StatisticsComputation(int uniqueValueCount, String mostCommonValue,
            int employeesWithValue, int employeesWithoutValue, String distribution) {
        this.uniqueValueCount = uniqueValueCount;
        this.mostCommonValue = mostCommonValue;
        this.employeesWithValue = employeesWithValue;
        this.employeesWithoutValue = employeesWithoutValue;
        this.distribution = distribution;
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

    public String getDistribution() {
        return distribution;
    }
}

