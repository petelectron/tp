package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.StatisticsService;
import seedu.address.model.Statistics;
import seedu.address.model.StatisticsMode;
import seedu.address.model.person.Person;

/**
 * Panel that displays statistics about employee records.
 * Only responsible for UI display - statistics calculation is delegated.
 */
public class StatsPanel extends UiPart<Region> {

    private static final String FXML = "StatsPanel.fxml";
    private static final Logger logger = LogsCenter.getLogger(StatsPanel.class);
    private static final String ACTIVE_LEGEND_STYLE = "-fx-text-fill: #2e7d32; -fx-font-weight: bold;";
    private static final String INACTIVE_LEGEND_STYLE = "-fx-text-fill: #808080; -fx-font-weight: normal;";

    @FXML
    private Label tagLegendLabel;

    @FXML
    private Label deptLegendLabel;

    @FXML
    private Label roleLegendLabel;

    @FXML
    private Label uniqueMetricTextLabel;

    @FXML
    private Label mostCommonMetricTextLabel;

    @FXML
    private Label employeesWithMetricTextLabel;

    @FXML
    private Region employeesWithRow;

    @FXML
    private Label employeesWithoutMetricTextLabel;

    @FXML
    private Region employeesWithoutRow;

    @FXML
    private Label distributionHeaderLabel;

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
    private StatisticsMode currentMode;

    /**
     * Creates a StatsPanel.
     *
     * @param logic The Logic component to get employee data from
     */
    public StatsPanel(Logic logic) {
        super(FXML);
        requireNonNull(logic);

        this.statisticsService = new StatisticsService(logic);
        this.currentMode = StatisticsMode.DEPARTMENT;

        // Listen for changes to the full address book list used by StatisticsService.
        logic.getAddressBook().getPersonList().addListener((ListChangeListener<Person>) change -> {
            logger.fine("Address book list changed, refreshing statistics");
            refresh();
        });

        refresh();
        logger.info("StatsPanel initialized successfully");
    }

    /**
     * Sets the current dashboard mode and refreshes the panel.
     */
    public void setStatisticsMode(StatisticsMode statisticsMode) {
        requireNonNull(statisticsMode);
        if (currentMode == statisticsMode) {
            return;
        }

        logger.fine("Switching statistics dashboard mode to: " + statisticsMode.getFullName());
        currentMode = statisticsMode;
        refresh();
    }

    /**
     * Refreshes all statistics in the panel.
     */
    private void refresh() {
        logger.finer("Refreshing statistics panel");
        Statistics stats = statisticsService.getCurrentStatistics(currentMode);
        updateUi(stats);
        logger.finer("Statistics panel refresh completed");
    }

    /**
     * Updates UI with the given statistics.
     * This method only handles UI updates, no calculations.
     */
    private void updateUi(Statistics stats) {
        requireNonNull(stats);
        assert totalEmployeesLabel != null : "totalEmployeesLabel not injected from FXML";
        assert uniqueTagsLabel != null : "uniqueTagsLabel not injected from FXML";
        assert mostCommonTagLabel != null : "mostCommonTagLabel not injected from FXML";
        assert employeesWithTagsLabel != null : "employeesWithTagsLabel not injected from FXML";
        assert employeesWithoutTagsLabel != null : "employeesWithoutTagsLabel not injected from FXML";
        assert tagDistributionLabel != null : "tagDistributionLabel not injected from FXML";

        updateModeLabels();

        totalEmployeesLabel.setText(String.valueOf(stats.getTotalEmployees()));
        uniqueTagsLabel.setText(String.valueOf(stats.getUniqueValueCount()));
        mostCommonTagLabel.setText(stats.getMostCommonValue());
        employeesWithTagsLabel.setText(String.valueOf(stats.getEmployeesWithValue()));
        employeesWithoutTagsLabel.setText(String.valueOf(stats.getEmployeesWithoutValue()));
        tagDistributionLabel.setText(stats.getValueDistribution());

        logger.finer("UI updated with: " + stats.getTotalEmployees() + " employees, "
                + stats.getUniqueValueCount() + " unique values in " + currentMode.getFullName() + " mode");
    }

    private void updateModeLabels() {
        switch (currentMode) {
        case TAG:
            uniqueMetricTextLabel.setText("🏷️ Unique tags:");
            mostCommonMetricTextLabel.setText("📈 Most common tag:");
            distributionHeaderLabel.setText("📋 Tag Distribution");
            employeesWithMetricTextLabel.setText("✅ Employees with tags:");
            employeesWithRow.setManaged(true);
            employeesWithRow.setVisible(true);
            employeesWithoutRow.setManaged(true);
            employeesWithoutRow.setVisible(true);
            tagLegendLabel.setStyle(ACTIVE_LEGEND_STYLE);
            deptLegendLabel.setStyle(INACTIVE_LEGEND_STYLE);
            roleLegendLabel.setStyle(INACTIVE_LEGEND_STYLE);
            break;
        case DEPARTMENT:
            uniqueMetricTextLabel.setText("🏢 Unique dept:");
            mostCommonMetricTextLabel.setText("📈 Most common dept:");
            distributionHeaderLabel.setText("📋 Dept Distribution");
            employeesWithRow.setManaged(false);
            employeesWithRow.setVisible(false);
            employeesWithoutRow.setManaged(false);
            employeesWithoutRow.setVisible(false);
            tagLegendLabel.setStyle(INACTIVE_LEGEND_STYLE);
            deptLegendLabel.setStyle(ACTIVE_LEGEND_STYLE);
            roleLegendLabel.setStyle(INACTIVE_LEGEND_STYLE);
            break;
        case ROLE:
            uniqueMetricTextLabel.setText("👤 Unique roles:");
            mostCommonMetricTextLabel.setText("📈 Most common role:");
            distributionHeaderLabel.setText("📋 Role Distribution");
            employeesWithRow.setManaged(false);
            employeesWithRow.setVisible(false);
            employeesWithoutRow.setManaged(false);
            employeesWithoutRow.setVisible(false);
            tagLegendLabel.setStyle(INACTIVE_LEGEND_STYLE);
            deptLegendLabel.setStyle(INACTIVE_LEGEND_STYLE);
            roleLegendLabel.setStyle(ACTIVE_LEGEND_STYLE);
            break;
        default:
            throw new AssertionError("Unknown StatisticsMode: " + currentMode);
        }
    }
}
