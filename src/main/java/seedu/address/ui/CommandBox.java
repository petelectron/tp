package seedu.address.ui;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;

    private final List<String> commandHistory = new ArrayList<>();
    private int historyIndex = 0;
    private String pendingInput = "";

    @FXML
    private TextField commandTextField;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> setStyleToDefault());
        commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
    }

    /**
     * Handles key press events for history navigation (UP/DOWN arrows).
     */
    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
        case UP:
            navigateHistory(-1);
            event.consume(); // prevent cursor jumping to start of text field
            break;
        case DOWN:
            navigateHistory(1);
            event.consume(); // prevent cursor jumping to end of text field
            break;
        default:
            break;
        }
    }

    /**
     * Navigates command history by the given direction.
     * direction=-1 goes back (older), direction=1 goes forward (newer).
     */
    private void navigateHistory(int direction) {

        // no history to navigate, do nothing
        if (commandHistory.isEmpty()) {
            return;
        }

        // Save whatever the user is currently typing before navigating away
        if (historyIndex == commandHistory.size() && direction == -1) {
            pendingInput = commandTextField.getText();
        }

        int newIndex = historyIndex + direction;
        // already at the end of history in the given direction, do nothing
        if (newIndex < 0 || newIndex > commandHistory.size()) {
            return;
        }
        historyIndex = newIndex;

        if (historyIndex == commandHistory.size()) { // arrived at pending (current position)
            commandTextField.setText(pendingInput);
        } else {
            commandTextField.setText(commandHistory.get(historyIndex));
        }
        commandTextField.positionCaret(commandTextField.getText().length());
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);

            // if history is empty or last entry is not the same as current, add to history
            if (commandHistory.isEmpty() || !commandHistory.get(commandHistory.size() - 1).equals(commandText)) {
                commandHistory.add(commandText);
                if (commandHistory.size() > 5) { // Only keep the 5 most recent entries; O(5) = O(1) time complexity
                    commandHistory.remove(0); // drop oldest entry
                }
            }
            historyIndex = commandHistory.size(); // reset history index to end
            pendingInput = "";
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();

        if (styleClass.contains(ERROR_STYLE_CLASS)) {
            return;
        }

        styleClass.add(ERROR_STYLE_CLASS);
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
