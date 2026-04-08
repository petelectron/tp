package seedu.address.ui;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    private static final String LABEL_PHONE = "Phone: ";
    private static final String LABEL_ROLE = "Role: ";
    private static final String LABEL_DEPARTMENT = "Department: ";
    private static final String LABEL_EMAIL = "Email: ";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on upstream level 4</a>
     */

    private final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label role;
    @FXML
    private Label department;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        requireNonNull(person);

        // Defensive assertions to ensure attributes are non-null
        requireNonNull(person.getName(), "Person should have a name");
        requireNonNull(person.getPhone(), "Person should have a phone");
        requireNonNull(person.getRole(), "Person should have a role");
        requireNonNull(person.getDepartment(), "Person should have a department");
        requireNonNull(person.getEmail(), "Person should have an email");
        requireNonNull(person.getTags(), "Person should have a tags set");

        this.person = person;

        id.setText(displayedIndex + ". ");
        name.setText(person.getName().getFullName());
        phone.setText(LABEL_PHONE + person.getPhone().getValue());
        role.setText(LABEL_ROLE + person.getRole().getValue());
        department.setText(LABEL_DEPARTMENT + person.getDepartment().getValue());
        email.setText(LABEL_EMAIL + person.getEmail().getValue());
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.getTagName()))
                .forEach(tag -> tags.getChildren().add(new Label(tag.getTagName())));
    }

    public Person getPerson() {
        return person;
    }
}
