package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.StatCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.StatisticsMode;

/**
 * Parses input arguments and creates a new {@link StatCommand} object.
 */
public class StatCommandParser implements Parser<StatCommand> {

    @Override
    public StatCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty() || trimmedArgs.split("\\s+").length != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, StatCommand.MESSAGE_USAGE));
        }

        StatisticsMode statisticsMode = StatisticsMode.fromUserInput(trimmedArgs)
                .orElseThrow(() -> new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, StatCommand.MESSAGE_USAGE)));
        return new StatCommand(statisticsMode);
    }
}

