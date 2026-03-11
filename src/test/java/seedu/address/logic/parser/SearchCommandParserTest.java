package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class SearchCommandParserTest {

    private SearchCommandParser parser = new SearchCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_keywordTooLong_throwsParseException() {
        String longKeyword = "a".repeat(SearchCommand.MAX_KEYWORD_LENGTH + 1);
        assertParseFailure(parser, longKeyword,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_tooManyKeywords_throwsParseException() {
        String tooManyKeywords = String.join(" ", Collections.nCopies(SearchCommand.MAX_KEYWORD_COUNT + 1, "a"));
        assertParseFailure(parser, tooManyKeywords,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_maxKeywordLength_returnsSearchCommand() {
        String maxLengthKeyword = "a".repeat(SearchCommand.MAX_KEYWORD_LENGTH);
        SearchCommand expectedSearchCommand =
                new SearchCommand(new NameContainsKeywordsPredicate(Arrays.asList(maxLengthKeyword)));
        assertParseSuccess(parser, maxLengthKeyword, expectedSearchCommand);
    }

    @Test
    public void parse_maxKeywordCount_returnsSearchCommand() {
        String validInput = String.join(" ", Collections.nCopies(SearchCommand.MAX_KEYWORD_COUNT, "a"));
        SearchCommand expectedSearchCommand =
                new SearchCommand(new NameContainsKeywordsPredicate(Collections.nCopies(SearchCommand.MAX_KEYWORD_COUNT,
                        "a")));
        assertParseSuccess(parser, validInput, expectedSearchCommand);
    }

    @Test
    public void parse_validArgs_returnsSearchCommand() {
        // no leading and trailing whitespaces
        SearchCommand expectedSearchCommand =
                new SearchCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedSearchCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedSearchCommand);
    }

}
