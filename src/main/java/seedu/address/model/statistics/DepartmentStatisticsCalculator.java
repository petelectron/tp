package seedu.address.model.statistics;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Person;

/**
 * Calculates statistics using person departments.
 */
public class DepartmentStatisticsCalculator extends AbstractValueStatisticsCalculator {

    @Override
    protected Set<String> extractValues(Person person) {
        Set<String> departmentValues = new HashSet<>();
        String departmentValue = person.getDepartment().value;
        if (!departmentValue.isBlank()) {
            departmentValues.add(departmentValue);
        }
        return departmentValues;
    }

    @Override
    protected String getEmptyDistributionMessage() {
        return "No departments yet";
    }
}

