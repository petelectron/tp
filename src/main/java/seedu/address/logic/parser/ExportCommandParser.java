package seedu.address.logic.parser;

import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.parser.exceptions.ParseException;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

/**
 * Parses input arguments and creates a new ExportCommand object
 */
public class ExportCommandParser implements Parser<ExportCommand> {

    /**
     * Parses {@code args} into an {@link ExportCommand}.
     *
     * @param args the argument string following the command word (leading/trailing whitespace stripped)
     * @throws ParseException if {@code args} is blank
     */
    @Override
    public ExportCommand parse(String args) throws ParseException {
        String trimmed = args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ExportCommand.MESSAGE_USAGE));
        }
        return new ExportCommand(trimmed);
    }
}
