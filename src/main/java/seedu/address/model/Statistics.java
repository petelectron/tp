package seedu.address.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Represents statistics calculated from employee records.
 * This is a data container with no UI logic.
 */
public class Statistics {

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
        this.totalEmployees = persons.size();

        // Calculate tag statistics
        Set<String> uniqueTags = new java.util.HashSet<>();
        Map<String, Integer> tagFrequency = new HashMap<>();
        int employeesWithTagsCount = 0;

        for (Person person : persons) {
            Set<Tag> personTags = person.getTags();
            if (!personTags.isEmpty()) {
                employeesWithTagsCount++;
                for (Tag tag : personTags) {
                    String tagName = tag.tagName;
                    uniqueTags.add(tagName);
                    tagFrequency.put(tagName, tagFrequency.getOrDefault(tagName, 0) + 1);
                }
            }
        }

        this.employeesWithTags = employeesWithTagsCount;
        this.employeesWithoutTags = totalEmployees - employeesWithTagsCount;
        this.uniqueTagCount = uniqueTags.size();
        this.mostCommonTag = findMostCommonTag(tagFrequency);
        this.tagDistribution = createTagDistribution(tagFrequency);
    }

    /**
     * Finds the most common tag from the frequency map.
     * @param tagFrequency Map of tag names to their frequencies
     * @return String representation of the most common tag with count, or "None" if no tags exist
     */
    private String findMostCommonTag(Map<String, Integer> tagFrequency) {
        if (tagFrequency.isEmpty()) {
            return "None";
        }

        String mostCommon = "";
        int maxFrequency = 0;

        for (Map.Entry<String, Integer> entry : tagFrequency.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                maxFrequency = entry.getValue();
                mostCommon = entry.getKey();
            }
        }

        return mostCommon + " (" + maxFrequency + ")";
    }

    /**
     * Creates a formatted string of the top 5 tags by frequency.
     * @param tagFrequency Map of tag names to their frequencies
     * @return Formatted string of tag distribution, or "No tags yet" if no tags exist
     */
    private String createTagDistribution(Map<String, Integer> tagFrequency) {
        if (tagFrequency.isEmpty()) {
            return "No tags yet";
        }

        return tagFrequency.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .map(entry -> "• " + entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining("\n"));
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