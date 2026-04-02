package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.StatisticsMode;
import seedu.address.model.UserPrefs;

public class StatCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullMode_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StatCommand(null));
    }

    @Test
    public void execute_tagMode_returnsCorrectFeedbackAndMode() {
        StatCommand command = new StatCommand(StatisticsMode.TAG);
        CommandResult result = command.execute(model);

        assertEquals(
                String.format(StatCommand.MESSAGE_SUCCESS, StatisticsMode.TAG.getFullName()),
                result.getFeedbackToUser());
        assertEquals(Optional.of(StatisticsMode.TAG), result.getStatisticsMode());
        assertFalse(result.isShowHelp());
        assertFalse(result.isExit());
    }

    @Test
    public void execute_departmentMode_returnsCorrectFeedbackAndMode() {
        StatCommand command = new StatCommand(StatisticsMode.DEPARTMENT);
        CommandResult result = command.execute(model);

        assertEquals(
                String.format(StatCommand.MESSAGE_SUCCESS, StatisticsMode.DEPARTMENT.getFullName()),
                result.getFeedbackToUser());
        assertEquals(Optional.of(StatisticsMode.DEPARTMENT), result.getStatisticsMode());
        assertFalse(result.isShowHelp());
        assertFalse(result.isExit());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        StatCommand command = new StatCommand(StatisticsMode.TAG);
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void execute_resultToString_containsStatisticsMode() {
        StatCommand command = new StatCommand(StatisticsMode.TAG);
        CommandResult result = command.execute(model);
        assertTrue(result.toString().contains("statisticsMode=TAG"));
    }
}
