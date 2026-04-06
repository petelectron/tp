package seedu.address.model.statistics;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Person;

/**
 * Calculates statistics using person roles.
 */
public class RoleStatisticsCalculator extends AbstractValueStatisticsCalculator {

    @Override
    protected Set<String> extractValues(Person person) {
        Set<String> roleValues = new HashSet<>();
        String roleValue = person.getRole().value;
        if (!roleValue.isBlank()) {
            roleValues.add(roleValue);
        }
        return roleValues;
    }

    @Override
    protected String getEmptyDistributionMessage() {
        return "No roles yet";
    }
}

