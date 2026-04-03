package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.StatCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.StatisticsMode;
import seedu.address.model.UserPrefs;

public class StatCommandParserTest {

    private final StatCommandParser parser = new StatCommandParser();
    private final String expectedMessage =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, StatCommand.MESSAGE_USAGE);

    @Test
    public void parse_shorthandT_returnsTagCommand() throws Exception {
        assertParsesToMode("t", StatisticsMode.TAG);
    }

    @Test
    public void parse_fullWordTag_returnsTagCommand() throws Exception {
        assertParsesToMode("tag", StatisticsMode.TAG);
    }

    @Test
    public void parse_tagWithWhitespace_returnsTagCommand() throws Exception {
        assertParsesToMode("  tag  ", StatisticsMode.TAG);
    }

    @Test
    public void parse_shorthandD_returnsDepartmentCommand() throws Exception {
        assertParsesToMode("d", StatisticsMode.DEPARTMENT);
    }

    @Test
    public void parse_aliasDept_returnsDepartmentCommand() throws Exception {
        assertParsesToMode("dept", StatisticsMode.DEPARTMENT);
    }

    @Test
    public void parse_fullWordDepartment_returnsDepartmentCommand() throws Exception {
        assertParsesToMode("department", StatisticsMode.DEPARTMENT);
    }

    @Test
    public void parse_departmentWithWhitespace_returnsDepartmentCommand() throws Exception {
        assertParsesToMode("  department  ", StatisticsMode.DEPARTMENT);
    }

    @Test
    public void parse_shorthandR_returnsRoleCommand() throws Exception {
        assertParsesToMode("r", StatisticsMode.ROLE);
    }

    @Test
    public void parse_fullWordRole_returnsRoleCommand() throws Exception {
        assertParsesToMode("role", StatisticsMode.ROLE);
    }

    @Test
    public void parse_roleWithWhitespace_returnsRoleCommand() throws Exception {
        assertParsesToMode("  role  ", StatisticsMode.ROLE);
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "", expectedMessage);
    }

    @Test
    public void parse_whitespaceOnly_throwsParseException() {
        assertParseFailure(parser, "   ", expectedMessage);
    }

    @Test
    public void parse_unknownMode_throwsParseException() {
        assertParseFailure(parser, "invalid", expectedMessage);
    }

    @Test
    public void parse_multipleWords_throwsParseException() {
        assertParseFailure(parser, "tag department", expectedMessage);
    }

    @Test
    public void parse_tooManyArgs_throwsParseException() {
        assertParseFailure(parser, "t d", expectedMessage);
    }

    private void assertParsesToMode(String userInput, StatisticsMode expectedMode) throws Exception {
        StatCommand command = parser.parse(userInput);
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        CommandResult result = command.execute(model);
        assertEquals(expectedMode, result.getStatisticsMode().orElseThrow());
    }
}
