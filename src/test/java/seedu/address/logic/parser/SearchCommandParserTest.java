package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_SEARCH_EXCEED_MAX_KEYWORDS;
import static seedu.address.logic.Messages.MESSAGE_SEARCH_KEYWORD_NOT_ALPHANUMERIC;
import static seedu.address.logic.Messages.MESSAGE_SEARCH_KEYWORD_TOO_LONG;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SearchCommand;
import seedu.address.model.person.PersonMatchesKeywordPredicate;

public class SearchCommandParserTest {

    private SearchCommandParser parser = new SearchCommandParser();

    @Test
    public void parse_nullArg_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> parser.parse(null));
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_keywordTooLong_throwsParseException() {
        String longKeyword = "a".repeat(SearchCommand.MAX_KEYWORD_LENGTH + 1);
        assertParseFailure(parser, longKeyword,
                String.format(MESSAGE_SEARCH_KEYWORD_TOO_LONG,
                    SearchCommand.MAX_KEYWORD_LENGTH, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_tooManyKeywords_throwsParseException() {
        assertParseFailure(parser, "a b c d e f",
                String.format(MESSAGE_SEARCH_EXCEED_MAX_KEYWORDS,
                    SearchCommand.MAX_KEYWORDS, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonAlphanumericKeyword_throwsParseException() {
        assertParseFailure(parser, "Alice-123",
                String.format(MESSAGE_SEARCH_KEYWORD_NOT_ALPHANUMERIC, SearchCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "ab-",
            String.format(MESSAGE_SEARCH_KEYWORD_NOT_ALPHANUMERIC, SearchCommand.MESSAGE_USAGE));

        assertParseFailure(parser, "ab_cd",
            String.format(MESSAGE_SEARCH_KEYWORD_NOT_ALPHANUMERIC, SearchCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_maxKeywordLength_returnsSearchCommand() {
        String maxLengthKeyword = "a".repeat(SearchCommand.MAX_KEYWORD_LENGTH);
        SearchCommand expectedSearchCommand =
                new SearchCommand(new PersonMatchesKeywordPredicate(Collections.singletonList(maxLengthKeyword)));
        assertParseSuccess(parser, maxLengthKeyword, expectedSearchCommand);
    }

    @Test
    public void parse_alphanumericKeyword_returnsSearchCommand() {
        String validInput = "a1B2c";
        SearchCommand expectedSearchCommand =
                new SearchCommand(new PersonMatchesKeywordPredicate(Collections.singletonList(validInput)));
        assertParseSuccess(parser, validInput, expectedSearchCommand);
    }

    @Test
    public void parse_multipleValidKeywords_returnsSearchCommand() {
        SearchCommand expectedSearchCommand =
            new SearchCommand(new PersonMatchesKeywordPredicate(java.util.List.of("Alice", "Bob", "12c")));
        assertParseSuccess(parser, "Alice Bob 12c", expectedSearchCommand);
    }

    @Test
    public void parse_validArgs_returnsSearchCommand() {
        SearchCommand expectedSearchCommand =
                new SearchCommand(new PersonMatchesKeywordPredicate(Collections.singletonList("Alice")));
        assertParseSuccess(parser, "Alice", expectedSearchCommand);
        assertParseSuccess(parser, "  \n Alice \t  ", expectedSearchCommand);
    }

}
