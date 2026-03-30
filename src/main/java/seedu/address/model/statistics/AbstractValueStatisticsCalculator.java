package seedu.address.model.statistics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.person.Person;

/**
 * Base calculator that aggregates values extracted from each person.
 */
public abstract class AbstractValueStatisticsCalculator implements StatisticsCalculator {

    private static final int TOP_DISTRIBUTION_LIMIT = 5;

    /**
     * Returns values contributed by one person for this calculation mode.
     */
    protected abstract Set<String> extractValues(Person person);

    /**
     * Returns the empty distribution message for this mode.
     */
    protected abstract String getEmptyDistributionMessage();

    @Override
    public StatisticsComputation compute(List<Person> persons) {
        assert persons != null : "Person list cannot be null";

        Set<String> uniqueValues = new HashSet<>();
        Map<String, Integer> valueFrequency = new HashMap<>();
        int employeesWithValuesCount = 0;

        for (Person person : persons) {
            assert person != null : "Person in list cannot be null";

            Set<String> values = extractValues(person);
            if (!values.isEmpty()) {
                employeesWithValuesCount++;
                for (String value : values) {
                    uniqueValues.add(value);
                    valueFrequency.put(value, valueFrequency.getOrDefault(value, 0) + 1);
                }
            }
        }

        int employeesWithoutValuesCount = persons.size() - employeesWithValuesCount;
        assert employeesWithValuesCount + employeesWithoutValuesCount == persons.size()
                : "Employee counts inconsistent";

        return new StatisticsComputation(
                uniqueValues.size(),
                findMostCommonValue(valueFrequency),
                employeesWithValuesCount,
                employeesWithoutValuesCount,
                createDistribution(valueFrequency));
    }

    private String findMostCommonValue(Map<String, Integer> valueFrequency) {
        if (valueFrequency.isEmpty()) {
            return "None";
        }

        String mostCommon = "";
        int maxFrequency = 0;

        for (Map.Entry<String, Integer> entry : valueFrequency.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostCommon = entry.getKey();
            }
        }

        return mostCommon + " (" + maxFrequency + ")";
    }

    private String createDistribution(Map<String, Integer> valueFrequency) {
        if (valueFrequency.isEmpty()) {
            return getEmptyDistributionMessage();
        }

        return valueFrequency.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(TOP_DISTRIBUTION_LIMIT)
                .map(entry -> "• " + entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }
}

