package seedu.address.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Represents statistics calculated from employee records.
 * This is a data container with no UI logic.
 */
public class Statistics {

    private static final Logger logger = LogsCenter.getLogger(Statistics.class);

    private final int totalEmployees;
    private final int uniqueTagCount;
    private final String mostCommonTag;
    private final int employeesWithTags;
    private final int employeesWithoutTags;
    private final String tagDistribution;

    /**
     * Creates a Statistics object by calculating from a list of persons.
     * @param persons The list of persons to calculate statistics from
     */
    public Statistics(List<Person> persons) {
        assert persons != null : "Person list cannot be null";

        this.totalEmployees = persons.size();
        logger.fine("Calculating statistics for " + totalEmployees + " employees");

        // Calculate tag statistics
        Set<String> uniqueTags = new java.util.HashSet<>();
        Map<String, Integer> tagFrequency = new HashMap<>();
        int employeesWithTagsCount = 0;

        for (Person person : persons) {
            assert person != null : "Person in list cannot be null";

            Set<Tag> personTags = person.getTags();
            assert personTags != null : "Person tags cannot be null";

            if (!personTags.isEmpty()) {
                employeesWithTagsCount++;
                for (Tag tag : personTags) {
                    assert tag != null : "Tag cannot be null";
                    assert tag.tagName != null : "Tag name cannot be null";

                    String tagName = tag.tagName;
                    uniqueTags.add(tagName);
                    tagFrequency.put(tagName, tagFrequency.getOrDefault(tagName, 0) + 1);
                }
            }
        }

        this.employeesWithTags = employeesWithTagsCount;
        this.employeesWithoutTags = totalEmployees - employeesWithTagsCount;
        assert employeesWithTags + employeesWithoutTags == totalEmployees : "Employee counts inconsistent";

        this.uniqueTagCount = uniqueTags.size();
        assert uniqueTagCount >= 0 : "Unique tag count cannot be negative";

        this.mostCommonTag = findMostCommonTag(tagFrequency);
        this.tagDistribution = createTagDistribution(tagFrequency);

        logger.fine("Statistics calculated: " + uniqueTagCount + " unique tags, "
                + employeesWithTags + " employees with tags");
    }

    /**
     * Finds the most common tag from the frequency map.
     * @param tagFrequency Map of tag names to their frequencies
     * @return String representation of the most common tag with count, or "None" if no tags exist
     */
    private String findMostCommonTag(Map<String, Integer> tagFrequency) {
        assert tagFrequency != null : "Tag frequency map cannot be null";

        if (tagFrequency.isEmpty()) {
            logger.fine("No tags found in data");
            return "None";
        }

        String mostCommon = "";
        int maxFrequency = 0;

        for (Map.Entry<String, Integer> entry : tagFrequency.entrySet()) {
            assert entry.getKey() != null : "Tag name in entry is null";
            assert entry.getValue() != null && entry.getValue() > 0 : "Tag frequency should be positive";

            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostCommon = entry.getKey();
            }
        }

        assert maxFrequency > 0 : "Max frequency should be positive";
        assert !mostCommon.isEmpty() : "Most common tag should not be empty when tags exist";

        String result = mostCommon + " (" + maxFrequency + ")";
        logger.fine("Most common tag: " + result);
        return result;
    }

    /**
     * Creates a formatted string of the top 5 tags by frequency.
     * @param tagFrequency Map of tag names to their frequencies
     * @return Formatted string of tag distribution, or "No tags yet" if no tags exist
     */
    private String createTagDistribution(Map<String, Integer> tagFrequency) {
        assert tagFrequency != null : "Tag frequency map cannot be null";

        if (tagFrequency.isEmpty()) {
            logger.fine("No tags to display in distribution");
            return "No tags yet";
        }

        String distribution = tagFrequency.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> "• " + entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));

        assert distribution != null : "Tag distribution should not be null";
        assert !distribution.isEmpty() : "Tag distribution should not be empty when tags exist";

        logger.fine("Tag distribution created with " + distribution.split("\n").length + " entries");
        return distribution;
    }

    // Getters
    public int getTotalEmployees() {
        return totalEmployees;
    }

    public int getUniqueTagCount() {
        return uniqueTagCount;
    }

    public String getMostCommonTag() {
        return mostCommonTag;
    }

    public int getEmployeesWithTags() {
        return employeesWithTags;
    }

    public int getEmployeesWithoutTags() {
        return employeesWithoutTags;
    }

    public String getTagDistribution() {
        return tagDistribution;
    }
}
