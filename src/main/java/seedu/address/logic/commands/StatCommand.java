package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;
import seedu.address.model.StatisticsMode;

/**
 * Switches the statistics dashboard mode.
 */
public class StatCommand extends Command {

    public static final String COMMAND_WORD = "stat";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Switches the HR statistics dashboard view.\n"
            + "Parameters: MODE (t|tag|d|dept|department)\n"
            + "Examples: " + COMMAND_WORD + " t, " + COMMAND_WORD + " d, or " + COMMAND_WORD + " dept";

    public static final String MESSAGE_SUCCESS = "Switched HR statistics dashboard to %s distribution.";

    private final StatisticsMode statisticsMode;

    /**
     * Creates a stat command that switches the dashboard to the specified mode.
     */
    public StatCommand(StatisticsMode statisticsMode) {
        requireNonNull(statisticsMode);
        this.statisticsMode = statisticsMode;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        return new CommandResult(
                String.format(MESSAGE_SUCCESS, statisticsMode.getFullName()),
                false,
                false,
                statisticsMode);
    }
}


