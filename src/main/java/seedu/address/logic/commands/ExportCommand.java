package seedu.address.logic.commands;

import seedu.address.commons.util.CsvExportUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * Exports current employee list into a csv file in the target destination.
 */
public class ExportCommand extends Command {

    public static final String COMMAND_WORD = "export";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Exports the current employee list into a local CSV file.\n"
        + "Parameters: Target file path "
        + "Example: " + COMMAND_WORD + "C:\\Users\\user\\Downloads\\employees.csv";

    public static final String MESSAGE_SUCCESS = "Exported app data to csv file";

    private static final String MESSAGE_IO_ERROR = "Could not write to file: %s\nCause: %s";
    public static final String MESSAGE_INVALID_PATH =
        "The provided file path is invalid: %s";
    public static final String MESSAGE_EMPTY_EXPORT =
        "Address book is empty — an empty CSV (header only) was written to: %s";

    private final String filePath;

    public ExportCommand(String filePath) {
        requireNonNull(filePath);
        this.filePath = filePath.trim();
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Path path = resolvePath();
        List<Person> persons = model.getAddressBook().getPersonList();

        writeCsv(persons, path);

        if (persons.isEmpty()) {
            return new CommandResult(
                String.format(MESSAGE_EMPTY_EXPORT, path.toAbsolutePath()));
        }

        return new CommandResult(
            String.format(MESSAGE_SUCCESS, persons.size(), path.toAbsolutePath()));
    }

    private Path resolvePath() throws CommandException {
        try {
            return Paths.get(filePath);
        } catch (InvalidPathException e) {
            throw new CommandException(String.format(MESSAGE_INVALID_PATH, filePath), e);
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
