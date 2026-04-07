package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import seedu.address.commons.util.CsvExportUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Exports current employee list into a csv file in the target destination.
 */
public class ExportCommand extends Command implements ConfirmableCommand {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Exports the current employee list into a local CSV file. "
        + "Parameters: Target file path\n"
        + "Example: " + COMMAND_WORD + " C:\\Users\\user\\Downloads\\employees.csv";

    public static final String MESSAGE_SUCCESS = "Exported app data to csv file";
    public static final String ACTION_SUMMARY = "Export full local list.";
    public static final String IMPACT_SUMMARY =
        "A csv file containing all current in-app employee data will be created at the target destination.";
    public static final String ACTION_DESCRIPTION = "export full local list";

    public static final String MESSAGE_EMPTY_EXPORT =
        "Address book is empty — an empty CSV (header only) was written to: %s";
    public static final String MESSAGE_INVALID_PATH =
        "The provided file path is invalid: %s";
    public static final String MESSAGE_FILE_ALREADY_EXISTS =
        "File already exists at target destination: %s.\nNo overwriting of local files is allowed.";
    public static final String MESSAGE_INVALID_EXTENSION =
        "Export only supports csv (comma-separated values) files. Ensure your file path ends in '.csv'.";
    private static final String MESSAGE_IO_ERROR = "Could not write to file: %s\nCause: %s";

    private final String filePath;
    private Path validatedPath;

    /**
     * Creates an ExportCommand
     */
    public ExportCommand(String filePath) {
        requireNonNull(filePath);
        this.filePath = filePath.trim();
    }

    @Override
    public String getConfirmationPrompt() {
        return ConfirmationPromptFormatter.format(ACTION_SUMMARY, IMPACT_SUMMARY);
    }

    @Override
    public String getActionDescription() {
        return ACTION_DESCRIPTION;
    }

    @Override
    public void validateBeforeConfirm(Model model) throws CommandException {
        requireNonNull(model);

        Path path = resolvePath();
        checkExistingFile(path);

        validatedPath = path;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Path path = validatedPath;
        List<Person> persons = model.getAddressBook().getPersonList();

        writeCsv(persons, path);

        if (persons.isEmpty()) {
            return new CommandResult(
                String.format(MESSAGE_EMPTY_EXPORT, path.toAbsolutePath()));
        }

        return new CommandResult(
            String.format(MESSAGE_SUCCESS, persons.size(), path.toAbsolutePath()));
    }

    /**
     * Resolves given path, throwing {@link CommandException} if it is invalid.
     */
    private Path resolvePath() throws CommandException {
        try {
            return Paths.get(filePath);
        } catch (InvalidPathException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_PATH, filePath), e);
        }
    }

    private void checkExistingFile(Path path) throws CommandException {
        if (Files.exists(path)) {
            throw new CommandException(String.format(MESSAGE_FILE_ALREADY_EXISTS, path));
        }
    }

    /**
     * Delegates to {@link CsvExportUtil} and translates {@link IOException} into {@link CommandException}.
     */
    private void writeCsv(List<Person> persons, Path path) throws CommandException {
        CsvExportUtil exporter = new CsvExportUtil();
        try {
            exporter.export(persons, path);
        } catch (IOException e) {
            throw new CommandException(
                String.format(MESSAGE_IO_ERROR, path, e.getMessage()), e);
        }
    }

}
