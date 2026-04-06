package seedu.address.commons.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.exceptions.CsvParseException;
import seedu.address.model.person.Person;

public class CsvImportUtilTest {
    @TempDir
    Path tempDir;

    private final CsvImportUtil parser = new CsvImportUtil();

    @Test
    void parse_validCsvStandardColumnOrder_success() throws Exception {
        Path csv = writeCsv(
            "name,phone,email,role,department,tags",
            "Alice Tan,91234567,alice@example.com,Software Engineer,Backend,Junior"
        );
        List<Person> persons = parser.parse(csv);
        assertEquals(1, persons.size());
        assertEquals("alice tan", persons.get(0).getName().fullName);
        assertEquals("91234567", persons.get(0).getPhone().value);
        assertEquals("alice@example.com", persons.get(0).getEmail().value);
    }

    @Test
    void parse_validCsvNonstandardColumnOrder_success() throws Exception {
        Path csv = writeCsv(
            "tags,phone,name,role,department,email",
            "junior,91234567,alice tan,software engineer,backend,alice@example.com"
        );
        List<Person> persons = parser.parse(csv);
        assertEquals(1, persons.size());
        assertEquals("alice tan", persons.get(0).getName().fullName);
        assertEquals("91234567", persons.get(0).getPhone().value);
        assertEquals("alice@example.com", persons.get(0).getEmail().value);
    }

    @Test
    void parse_multiplePeople_returnsAll() throws Exception {
        Path csv = writeCsv(
            "name,phone,email,role,department",
            "Alice Tan,91234567,alice@example.com,Software Engineer,Backend",
            "Bob Lee,98765432,bob@example.com,UI Designer,Design",
            "Carol Ng,81234567,carol@example.com,Head of Marketing,Marketing"
        );
        assertEquals(3, parser.parse(csv).size());
    }

    @Test
    void parse_multipleTagsQuoted_parsedCorrectly() throws Exception {
        Path csv = writeCsv(
            "name,phone,email,role,department,tags",
            "Alice,91234567,alice@example.com,Software Engineer,Backend,\"friends,colleagues\""
        );
        List<Person> persons = parser.parse(csv);
        assertEquals(2, persons.get(0).getTags().size());
    }

    @Test
    void parse_noTagsColumn_returnsEmptyTagSet() throws Exception {
        Path csv = writeCsv(
            "name,phone,email,role,department",
            "Alice,91234567,alice@example.com,Software Engineer,Backend"
        );
        assertTrue(parser.parse(csv).get(0).getTags().isEmpty());
    }

    @Test
    void parse_blankLinesInFile_skipped() throws Exception {
        Path csv = writeCsv(
            "name,phone,email,role,department",
            "",
            "Alice,91234567,alice@example.com,Software Engineer,Backend",
            ""
        );
        assertEquals(1, parser.parse(csv).size());
    }

    @Test
    void parse_headerCaseInsensitive_success() throws Exception {
        Path csv = writeCsv(
            "Name,PHONE,Email,ROLE,DepaRTMENT",
            "Alice,91234567,alice@example.com,Software Engineer,Backend"
        );
        assertEquals(1, parser.parse(csv).size());
    }

    @Test
    void parse_extraUnknownColumns_ignored() throws Exception {
        Path csv = writeCsv(
            "name,phone,email,role,department,age,birthday",
            "Alice,91234567,alice@example.com,Software Engineer,Backend,31,1998 Oct 30"
        );
        assertEquals(1, parser.parse(csv).size());
    }

    @Test
    void parse_emptyFile_throwsCsvParseException() {
        Path csv = writeRaw("");
        assertThrows(CsvParseException.class, () -> parser.parse(csv));
    }

    @Test
    void parse_invalidCsv_throwsCsvParseException() throws Exception {
        Path csv = writeCsv(
            "bags,pone,name,role,department,email",
            "Junior,91234567,Alice Tan,Software Engineer,Backend,alice@example.com"
        );
        assertThrows(CsvParseException.class, () -> parser.parse(csv));
    }

    @Test
    void parse_missingFields_throwsCsvParseException() throws Exception {
        Path csv = writeCsv(
            "name,role,department,email",
            "Alice Tan,Software Engineer,Backend,alice@example.com"
        );
        assertThrows(CsvParseException.class, () -> parser.parse(csv));
    }

    //helpers
    private Path writeCsv(String... lines) {
        return writeRaw(String.join("\n", lines));
    }

    private Path writeRaw(String content) {
        try {
            Path file = tempDir.resolve("test.csv");
            Files.writeString(file, content);
            return file;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
