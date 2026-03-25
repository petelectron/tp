package seedu.address.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.Statistics;
import seedu.address.model.person.Person;

public class StatisticsServiceTest {

    private TestLogic testLogic;
    private StatisticsService statisticsService;

    @BeforeEach
    public void setUp() {
        testLogic = new TestLogic();
        statisticsService = new StatisticsService(testLogic);
    }

    @Test
    public void constructor_nullLogic_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new StatisticsService(null));
    }

    @Test
    public void getCurrentStatistics_validLogic_returnsStatistics() {
        // Setup test data
        List<Person> persons = new ArrayList<>();
        persons.add(ALICE);
        persons.add(BENSON);
        testLogic.setPersonList(FXCollections.observableArrayList(persons));

        Statistics stats = statisticsService.getCurrentStatistics();

        assertNotNull(stats);
        assertEquals(2, stats.getTotalEmployees());
    }

    @Test
    public void getCurrentStatistics_emptyList_returnsEmptyStats() {
        testLogic.setPersonList(FXCollections.observableArrayList());

        Statistics stats = statisticsService.getCurrentStatistics();

        assertEquals(0, stats.getTotalEmployees());
        assertEquals("None", stats.getMostCommonTag());
        assertEquals("No tags yet", stats.getTagDistribution());
    }

    @Test
    public void getCurrentStatistics_updatesWhenListChanges() {
        // Start with empty list
        testLogic.setPersonList(FXCollections.observableArrayList());

        Statistics stats1 = statisticsService.getCurrentStatistics();
        assertEquals(0, stats1.getTotalEmployees());

        // Add a person
        List<Person> persons = new ArrayList<>();
        persons.add(ALICE);
        testLogic.setPersonList(FXCollections.observableArrayList(persons));

        Statistics stats2 = statisticsService.getCurrentStatistics();
        assertEquals(1, stats2.getTotalEmployees());
    }

    /**
     * Test implementation of Logic interface for testing StatisticsService.
     */
    private static class TestLogic implements Logic {

        private ObservableList<Person> personList = FXCollections.observableArrayList();

        void setPersonList(ObservableList<Person> personList) {
            this.personList = personList;
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return personList;
        }

        @Override
        public CommandResult execute(String commandText) throws CommandException, ParseException {
            throw new UnsupportedOperationException("execute not supported in TestLogic");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return null;
        }

        @Override
        public Path getAddressBookFilePath() {
            return null;
        }

        @Override
        public GuiSettings getGuiSettings() {
            return null;
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            // Not used
        }
    }
}
