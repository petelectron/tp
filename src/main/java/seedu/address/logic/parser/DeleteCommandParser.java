package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {

        String trimmedArgs = args.trim();

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        String[] tokens = trimmedArgs.split("\\s+");
        List<Index> indexes = new ArrayList<>();

        try {
            for (String token : tokens) {
                indexes.add(ParserUtil.parseIndex(token));
            }
        } catch (ParseException pe) {
            // Propagate specific index error message if it's an invalid index
            if (pe.getMessage().equals(seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX)) {
                throw pe;
            } else {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
            }
        }

        // Deduplicate before checking max index count
        long uniqueCount = indexes.stream().distinct().count();
        if (uniqueCount > DeleteCommand.MAX_INDEX_COUNT) {
            throw new ParseException("Too many indexes specified.");
        }

        return new DeleteCommand(indexes);
    }
}
