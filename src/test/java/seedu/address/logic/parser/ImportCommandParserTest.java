package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

public class ImportCommandParserTest {
    private final ImportCommandParser parser = new ImportCommandParser();

    @Test
    void parse_absoluteUnixPath_returnsImportCommand() throws Exception {
        ImportCommand cmd = parser.parse("/home/user/employees.csv");
        assertEquals(new ImportCommand("/home/user/employees.csv"), cmd);
    }

    @Test
    void parse_relativePathWithSubdirectory_returnsImportCommand() throws Exception {
        ImportCommand cmd = parser.parse("./data/employees.csv");
        assertEquals(new ImportCommand("./data/employees.csv"), cmd);
    }

    @Test
    void parse_emptyArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse(""));
    }

    @Test
    void parse_whitespaceOnlyArgs_throwsParseException() {
        assertThrows(ParseException.class, () -> parser.parse("   "));
    }
}
