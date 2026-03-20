package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.StatisticsService;
import seedu.address.model.Statistics;
import seedu.address.model.person.Person;

/**
 * Panel that displays statistics about employee records.
 * Only responsible for UI display - statistics calculation is delegated.
 */
public class StatsPanel extends UiPart<Region> {

    private static final String FXML = "StatsPanel.fxml";
    private static final Logger logger = LogsCenter.getLogger(StatsPanel.class);

    @FXML
    private Label totalEmployeesLabel;

    @FXML
    private Label uniqueTagsLabel;

    @FXML
    private Label mostCommonTagLabel;

    @FXML
    private Label employeesWithTagsLabel;

    @FXML
    private Label employeesWithoutTagsLabel;

    @FXML
    private Label tagDistributionLabel;

    private final StatisticsService statisticsService;
    private final Logic logic;

    /**
     * Creates a StatsPanel.
     *
     * @param logic The Logic component to get employee data from
     */
    public StatsPanel(Logic logic) {
        super(FXML);
        this.logic = logic;
        this.statisticsService = new StatisticsService(logic);

        // Listen for changes to the person list - using anonymous class
        logic.getFilteredPersonList().addListener(new ListChangeListener<Person>() {
            @Override
            public void onChanged(Change<? extends Person> change) {
                refresh();
            }
        });

        refresh();
        logger.info("StatsPanel initialized");
    }

    /**
     * Refreshes all statistics in the panel.
     */
    private void refresh() {
        Statistics stats = statisticsService.getCurrentStatistics();
        updateUi(stats);
    }

    /**
     * Updates UI with the given statistics.
     * This method only handles UI updates, no calculations.
     */
    private void updateUi(Statistics stats) {
        totalEmployeesLabel.setText(String.valueOf(stats.getTotalEmployees()));
        uniqueTagsLabel.setText(String.valueOf(stats.getUniqueTagCount()));
        mostCommonTagLabel.setText(stats.getMostCommonTag());
        employeesWithTagsLabel.setText(String.valueOf(stats.getEmployeesWithTags()));
        employeesWithoutTagsLabel.setText(String.valueOf(stats.getEmployeesWithoutTags()));
        tagDistributionLabel.setText(stats.getTagDistribution());
    }
}