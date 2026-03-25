package seedu.address.commons.util;

import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for serialising a list of {@link Person} objects to a CSV file.
 */
public class CsvExportUtil {
    public static final String CSV_HEADER = "name,phone,email,address,tags";

    /**
     * Writes {@code persons} to {@code csvPath} in CSV format.
     *
     * <p>If the file already exists it is overwritten. Parent directories are
     * created on demand.
     *
     * @param persons list of persons to export; must not be null
     * @param csvPath destination path; must not be null
     * @throws IOException if the file cannot be created or written
     */
    public void export(List<Person> persons, Path csvPath) throws IOException {
        // Create parent directories if needed (e.g. first-time export)
        Path parent = csvPath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter writer = Files.newBufferedWriter(csvPath)) {
            writer.write(CSV_HEADER);
            writer.newLine();

            for (Person person : persons) {
                writer.write(formatRow(person));
                writer.newLine();
            }
        }
    }

    /**
     * Converts a person into a CSV row string.
     */
    public String formatRow(Person person) {
        String name = quote(person.getName().fullName);
        String phone = quote(person.getPhone().value);
        String email = quote(person.getEmail().value);
        String role = quote(person.getRole().value);
        String tags = formatTags(person);

        return String.join(",", name, phone, email, role, tags);
    }

    private String formatTags(Person person) {
        List<String> tagNames = person.getTags().stream()
            .map(tag -> tag.tagName)
            .sorted() // deterministic order makes tests and diffs easier
            .collect(Collectors.toList());

        if (tagNames.isEmpty()) {
            return "";
        }
        return quote(String.join(",", tagNames));
    }

    private static String quote(String value) {
        return '"' + value.replace("\"", "\"\"") + '"';
    }
}
