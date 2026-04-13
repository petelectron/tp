package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DEPARTMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ROLE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.AddressBook.MAX_SIZE;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the HRmanager.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an employee to HRmanager. "
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ROLE + "ROLE "
            + PREFIX_DEPARTMENT + "DEPARTMENT "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ROLE + "Software Engineer "
            + PREFIX_DEPARTMENT + "Human Resources "
            + PREFIX_TAG + "junior";

    public static final String MESSAGE_SUCCESS = "New employee added: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This employee already exists in HRmanager";

    public static final String MESSAGE_OVER_LIMIT = String.format(
        "You are already at max capacity!\nHRmanager supports a maximum of %d employees.", MAX_SIZE);

    private static final Logger logger = LogsCenter.getLogger(AddCommand.class);

    private final Person toAdd;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        logger.info("Attempting to add person: " + toAdd.getName());

        if (model.getAddressBook().getPersonList().size() >= MAX_SIZE) {
            handleModelOverSizeLimit();
        }

        if (hasDuplicatePerson(model)) {
            handleDuplicatePersonInModel();
        }

        addPersonToModel(model);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAdd)));
    }

    private boolean hasDuplicatePerson(Model model) {
        return model.hasPerson(toAdd);
    }

    private void handleDuplicatePersonInModel() throws CommandException {
        logger.warning("Duplicate person attempted: " + toAdd.getName());
        throw new CommandException(MESSAGE_DUPLICATE_PERSON);
    }

    private void handleModelOverSizeLimit() throws CommandException {
        throw new CommandException(MESSAGE_OVER_LIMIT);
    }

    private void addPersonToModel(Model model) {
        model.addPerson(toAdd);
        logger.fine("Successfully added person: " + toAdd.getName());
        model.commitAddressBook();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddCommand)) {
            return false;
        }

        AddCommand otherAddCommand = (AddCommand) other;
        return toAdd.equals(otherAddCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
