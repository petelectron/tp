package seedu.address.logic;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Path;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.ConfirmableCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;
import seedu.address.storage.Storage;

/**
 * The main LogicManager of the app.
 */
public class LogicManager implements Logic {
    public static final String FILE_OPS_ERROR_FORMAT = "Could not save data due to the following error: %s";

    public static final String FILE_OPS_PERMISSION_ERROR_FORMAT =
            "Could not save data to file %s due to insufficient permissions to write to the file or the folder.";

    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Storage storage;
    private final AddressBookParser addressBookParser;
    private PendingConfirmation pendingConfirmation;

    /**
     * Constructs a {@code LogicManager} with the given {@code Model} and {@code Storage}.
     */
    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
        addressBookParser = new AddressBookParser();
    }

    @Override
    public CommandResult execute(String commandText) throws CommandException, ParseException {
        logger.info("----------------[USER COMMAND][" + commandText + "]");

        if (pendingConfirmation != null) {
            return handlePendingConfirmation(commandText);
        }

        Command command = addressBookParser.parseCommand(commandText);
        if (command instanceof ConfirmableCommand confirmableCommand) {
            pendingConfirmation = new PendingConfirmation(command, confirmableCommand.getActionDescription());
            return new CommandResult(confirmableCommand.getConfirmationPrompt());
        }

        return executeAndSave(command);
    }

    private CommandResult handlePendingConfirmation(String commandText) throws CommandException {
        requireNonNull(pendingConfirmation, "handlePendingConfirmation called without a pending command");

        String confirmationInput = commandText.trim();
        if (confirmationInput.equalsIgnoreCase("y")) {
            Command commandToExecute = pendingConfirmation.command();
            pendingConfirmation = null;
            return executeAndSave(commandToExecute);
        }

        if (confirmationInput.equalsIgnoreCase("n")) {
            String actionDescription = pendingConfirmation.actionDescription();
            pendingConfirmation = null;
            return new CommandResult(String.format(Messages.MESSAGE_COMMAND_CANCELLED, actionDescription));
        }

        return new CommandResult(Messages.MESSAGE_INVALID_CONFIRMATION_INPUT);
    }

    private CommandResult executeAndSave(Command command) throws CommandException {
        CommandResult commandResult = command.execute(model);

        try {
            storage.saveAddressBook(model.getAddressBook());
        } catch (AccessDeniedException e) {
            throw new CommandException(String.format(FILE_OPS_PERMISSION_ERROR_FORMAT, e.getMessage()), e);
        } catch (IOException ioe) {
            throw new CommandException(String.format(FILE_OPS_ERROR_FORMAT, ioe.getMessage()), ioe);
        }

        return commandResult;
    }

    /**
     * Represents a command awaiting user confirmation.
     */
    private record PendingConfirmation(Command command, String actionDescription) {
        public PendingConfirmation {
            requireNonNull(command);
            requireNonNull(actionDescription);
        }
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return model.getAddressBook();
    }

    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return model.getFilteredPersonList();
    }

    @Override
    public Path getAddressBookFilePath() {
        return model.getAddressBookFilePath();
    }

    @Override
    public GuiSettings getGuiSettings() {
        return model.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        model.setGuiSettings(guiSettings);
    }
}
