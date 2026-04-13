package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input and creates a new ImportCommand object.
 * Expects exactly one argument: the path to the CSV file.
 */
public class ImportCommandParser implements Parser<ImportCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ImportCommand
     * and returns an ImportCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public ImportCommand parse(String args) throws ParseException {
        String trimmed = args.trim();
        if (trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1);
        }
        if (trimmed.startsWith("~")) {
            trimmed = trimmed.replaceFirst("^~", System.getProperty("user.home"));
        } //for unix-based systems
        if (trimmed.isEmpty()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ImportCommand.MESSAGE_USAGE));
        }
        if (!trimmed.endsWith(".csv")) {
            throw new ParseException(
                String.format(ImportCommand.MESSAGE_INVALID_PATH, ImportCommand.MESSAGE_NOT_CSV));
        }
        return new ImportCommand(trimmed);
    }
}
