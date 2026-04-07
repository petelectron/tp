package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import seedu.address.commons.exceptions.CsvParseException;
import seedu.address.commons.util.CsvImportUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Imports an employee list from a local csv file, replacing the current data.
 */
public class ImportCommand extends Command implements ConfirmableCommand {

    public static final String COMMAND_WORD = "import";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports employee list from local CSV file, "
        + "replacing the current app data. "
        + "Parameters: file path of target csv file\n"
        + "Example: "
        + COMMAND_WORD + " C:\\Users\\user\\Downloads\\employees.csv";

    public static final String MESSAGE_SUCCESS = "Imported %d employee(s) from %s";
    public static final String ACTION_SUMMARY = "Import local list.";
    public static final String IMPACT_SUMMARY =
        "New employee list will be created from local data, overwriting existing import list.";
    public static final String ACTION_DESCRIPTION = "import local list";


    public static final String MESSAGE_FILE_NOT_FOUND =
        "File not found: %s\nPlease check that the path is correct and the file exists.";
    public static final String MESSAGE_NOT_A_FILE =
        "The path does not point to a file: %s";
    public static final String MESSAGE_INVALID_PATH =
        "The provided file path is invalid: %s";
    public static final String MESSAGE_NOT_CSV =
        "Only csv files are supported";
    public static final int MAX_KILOBYTES = 100;
    public static final int MAX_BYTES = 100000; //100kb
    public static final String MESSAGE_FILE_SIZE_OVER_LIMIT =
        String.format("Target file exceeds the limit of %d kB (%d bytes)", MAX_KILOBYTES, MAX_BYTES);
    public static final String MESSAGE_CSV_PARSE_ERROR =
        "Failed to parse CSV file — %s";
    public static final String MESSAGE_IO_ERROR =
        "Could not read file: %s\nCause: %s";
    public static final String MESSAGE_EMPTY_FILE =
        "Target file is empty!\nTo clear current list, use 'clear' command.";

    private final String filePath;
    private Path validatedPath;
    private List<Person> validatedPersons;

    /**
     * Constructs an ImportCommand instance given a file path.
     */
    public ImportCommand(String filePath) {
        requireNonNull(filePath);
        this.filePath = filePath;
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
        validatePath(path);

        validatedPath = path;
        validatedPersons = readCsv(path);

        if (validatedPersons.isEmpty()) {
            throw new CommandException(MESSAGE_EMPTY_FILE);
        }
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Path path = validatedPath;
        List<Person> persons = validatedPersons;

        // Clear cached pre-validation results after handoff to execution.
        validatedPath = null;
        validatedPersons = null;

        if (path == null || persons == null) {
            path = resolvePath();
            validatePath(path);
            persons = readCsv(path);
        }

        model.commitAddressBook();

        // Build a fresh address book and populate it atomically
        AddressBook newBook = new AddressBook();
        persons.forEach(newBook::addPerson);
        model.setAddressBook(newBook);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(
            String.format(MESSAGE_SUCCESS, persons.size(), path.toAbsolutePath()));
    }

    /**
     * Converts the raw string to a {@link Path}, throwing {@link CommandException} on failure.
     * */
    private Path resolvePath() throws CommandException {
        try {
            return Paths.get(filePath);
        } catch (InvalidPathException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_PATH, filePath), e);
        }
    }

    /**
     * Checks that {@code path} points to an existing regular file.
     * */
    private void validatePath(Path path) throws CommandException {
        if (!Files.exists(path)) {
            throw new CommandException(String.format(MESSAGE_FILE_NOT_FOUND, path));
        }
        if (!Files.isRegularFile(path)) {
            throw new CommandException(String.format(MESSAGE_NOT_A_FILE, path));
        }
        try {
            long bytes = Files.size(path);
            if (bytes > MAX_BYTES) {
                throw new CommandException(MESSAGE_FILE_SIZE_OVER_LIMIT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Delegates to {@link CsvImportUtil} and translates checked exceptions into {@link CommandException}.
     * */
    private List<Person> readCsv(Path path) throws CommandException {
        CsvImportUtil parser = new CsvImportUtil();
        try {
            return parser.parse(path);
        } catch (CsvParseException e) {
            throw new CommandException(
                String.format(MESSAGE_CSV_PARSE_ERROR, e.getMessage()), e);
        } catch (IOException e) {
            throw new CommandException(
                String.format(MESSAGE_IO_ERROR, path, e.getMessage()), e);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ImportCommand)) {
            return false;
        }

        ImportCommand otherImportCommand = (ImportCommand) other;
        return filePath.equals(otherImportCommand.filePath);
    }

}
