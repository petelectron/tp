package seedu.address.commons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.commons.exceptions.CsvParseException;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.model.person.Department;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

/**
 * Utility class for parsing a CSV file into a list of {@link Person} objects.
 */
public class CsvImportUtil {
    public static final int MAX_SIZE = 200;
    // Recognised header names (case-insensitive)
    private static final String HEADER_NAME = "name";
    private static final String HEADER_PHONE = "phone";
    private static final String HEADER_EMAIL = "email";
    private static final String HEADER_ROLE = "role";
    private static final String HEADER_DEPARTMENT = "department";
    private static final String HEADER_TAGS = "tags";

    /** Column indices. -1 means absent. */
    private int idxName = -1;
    private int idxPhone = -1;
    private int idxEmail = -1;
    private int idxRole = -1;
    private int idxDepartment = -1;
    private int idxTags = -1;

    /**
     * Parses {@code csvPath} and returns the list of persons found.
     *
     * @param csvPath path to the CSV file
     * @return list of {@link Person} instances; never null, may be empty
     * @throws IOException if the file cannot be read
     * @throws CsvParseException if the CSV is malformed (bad header, missing required
     *                           column, or invalid field value on any data row)
     */
    public List<Person> parse(Path csvPath) throws IOException, CsvParseException {
        List<Person> persons = new ArrayList<>();

        // Reset column indices for fresh parse
        idxName = -1;
        idxPhone = -1;
        idxEmail = -1;
        idxRole = -1;
        idxDepartment = -1;
        idxTags = -1;

        try (BufferedReader reader = Files.newBufferedReader(csvPath)) {
            String headerLine = null;
            int lineNumber = 0;
            int employeeNumber = 0;

            // Find the first non-blank line — treat it as the header
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (!line.isBlank()) {
                    headerLine = line;
                    break;
                }
            }

            if (headerLine == null) {
                throw new CsvParseException(ImportCommand.MESSAGE_EMPTY_FILE);
            }

            resolveHeaderIndices(headerLine, lineNumber);

            // Parse data rows
            List<String> names = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (employeeNumber >= MAX_SIZE) {
                    throw new CsvParseException(String.format(
                        "Your import file is too large.\nHRmanager supports a maximum of %d employees.", MAX_SIZE));
                }
                if (line.isBlank()) {
                    continue; // skip blank rows silently
                }
                Person person = parseDataRow(line, lineNumber);
                //check duplicates
                if (names.contains(person.getName().toString())) {
                    throw new CsvParseException(String.format(
                        "Employees with duplicate names on line %d and %d: %s",
                        names.indexOf(person.getName().toString()), lineNumber, person.getName()));
                }
                persons.add(person);
                employeeNumber++;
                names.add(person.getName().toString());
            }
        }

        return persons;
    }

    private void resolveHeaderIndices(String headerLine, int lineNumber) throws CsvParseException {
        List<String> headers = splitCsvLine(headerLine);

        for (int i = 0; i < headers.size(); i++) {

            String header = headers.get(i).toLowerCase().trim();
            //didn't put a switch here, not sure what to do for the default case
            if (header.equals(HEADER_NAME)) {
                idxName = i;
            } else if (header.equals(HEADER_PHONE)) {
                idxPhone = i;
            } else if (header.equals(HEADER_EMAIL)) {
                idxEmail = i;
            } else if (header.equals(HEADER_ROLE)) {
                idxRole = i;
            } else if (header.equals(HEADER_TAGS)) {
                idxTags = i;
            } else if (header.equals(HEADER_DEPARTMENT)) {
                idxDepartment = i;
            }
        }

        // Validate required columns exist
        List<String> missing = new ArrayList<>();
        if (idxName == -1) {
            missing.add(HEADER_NAME);
        }
        if (idxPhone == -1) {
            missing.add(HEADER_PHONE);
        }
        if (idxEmail == -1) {
            missing.add(HEADER_EMAIL);
        }
        if (idxRole == -1) {
            missing.add(HEADER_ROLE);
        }
        if (idxDepartment == -1) {
            missing.add(HEADER_DEPARTMENT);
        }

        if (!missing.isEmpty()) {
            throw new CsvParseException(String.format(
                "Line %d (header): missing required column(s): %s",
                lineNumber, String.join(", ", missing)));
        }
    }

    private Person parseDataRow(String line, int lineNumber) throws CsvParseException {
        List<String> fields = splitCsvLine(line);

        try {
            String nameStr = getField(fields, idxName, "name", lineNumber);
            String phoneStr = getField(fields, idxPhone, "phone", lineNumber);
            String emailStr = getField(fields, idxEmail, "email", lineNumber);
            String roleStr = getField(fields, idxRole, "role", lineNumber);
            String departmentStr = getField(fields, idxDepartment, "department", lineNumber);

            Name name = new Name(nameStr);
            Phone phone = new Phone(phoneStr);
            Email email = new Email(emailStr);
            Role role = new Role(roleStr);
            Department department = new Department(departmentStr);
            Set<Tag> tags = parseTags(fields, lineNumber);

            return new Person(name, phone, email, role, department, tags);

        } catch (IllegalArgumentException e) {
            // Thrown by Name/Phone/Email/Role/Department/Tag constructors on invalid input
            throw new CsvParseException(
                String.format("Line %d: invalid field value — %s", lineNumber, e.getMessage()), e);
        }
    }

    /**
     * Retrieves and trims the field at {@code index}, throwing if absent or blank.
     */
    private String getField(List<String> fields, int index, String fieldName, int lineNumber) throws CsvParseException {
        if (index >= fields.size()) {
            throw new CsvParseException(
                String.format("Line %d: missing column '%s'.", lineNumber, fieldName));
        }
        String value = fields.get(index).trim();
        if (value.isEmpty()) {
            throw new CsvParseException(
                String.format("Line %d: column '%s' must not be empty.", lineNumber, fieldName));
        }
        return value;
    }

    /**
     * Parses the optional tags column; returns an empty set if the column is absent.
     */
    private Set<Tag> parseTags(List<String> fields, int lineNumber) throws CsvParseException {
        if (idxTags == -1 || idxTags >= fields.size()) {
            return Set.of();
        }
        String raw = fields.get(idxTags).trim();
        if (raw.isEmpty()) {
            return Set.of();
        }
        try {
            Set<Tag> tags = Arrays.stream(raw.split(","))
                    .map(String::trim)
                    .filter(t -> !t.isEmpty())
                    .map(Tag::new)
                    .collect(Collectors.toSet());

            if (tags.size() > Person.MAX_TAG_COUNT) {
                throw new CsvParseException(
                        String.format("Line %d: " + Tag.MESSAGE_TAG_COUNT_CONSTRAINTS,
                                lineNumber, Person.MAX_TAG_COUNT, tags.size()));
            }

            return tags;
        } catch (IllegalArgumentException e) {
            throw new CsvParseException(
                String.format("Line %d: invalid tag — %s", lineNumber, e.getMessage()), e);
        }
    }

    /**
     * Splits a single CSV line respecting double-quoted fields.
     * Surrounding quotes are stripped; escaped double-quotes ({@code ""}) are unescaped.
     */
    static List<String> splitCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (inQuotes) {
                if (c == '"') {
                    // Peek ahead: "" → literal quote
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++; // skip the second quote
                    } else {
                        inQuotes = false; // closing quote
                    }
                } else {
                    current.append(c);
                }
            } else {
                if (c == '"') {
                    inQuotes = true;
                } else if (c == ',') {
                    fields.add(current.toString());
                    current.setLength(0);
                } else {
                    current.append(c);
                }
            }
        }
        fields.add(current.toString()); // last field
        return fields;
    }

}
