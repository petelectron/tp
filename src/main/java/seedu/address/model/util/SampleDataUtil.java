package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Department;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Role;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Aiden Tan"), new Phone("91456789"), new Email("Aiden.tan@gmail.com"),
                new Role("Analyst"),
                new Department("Finance"),
                getTagSet("junior")),
            new Person(new Name("Beatrice Lim"), new Phone("98123456"), new Email("beatrice.lim@yahoo.com"),
                new Role("Recruiter"),
                new Department("Human Resources"),
                getTagSet("mid-level")),
            new Person(new Name("Caleb Ong"), new Phone("91234567"), new Email("caleb.ong@gmail.com"),
                new Role("Engineer"),
                new Department("Technology"),
                getTagSet("mid-level")),
            new Person(new Name("Minh Lee"), new Phone("96543210"), new Email("minh.lee@gmail.com"),
                new Role("Coordinator"),
                new Department("Operations"),
                getTagSet("junior")),
            new Person(new Name("Ethan Lee"), new Phone("91789012"), new Email("ethan.lee@outlook.com"),
                new Role("Manager"),
                new Department("Sales"),
                getTagSet("lead")),
            new Person(new Name("Farah Noor"), new Phone("92876543"), new Email("farah.noor@gmail.com"),
                new Role("Specialist"),
                new Department("Legal"),
                getTagSet("senior")),
            new Person(new Name("Gavin Chia"), new Phone("91012345"), new Email("gavin.chia@yahoo.com"),
                new Role("Executive"),
                new Department("Marketing"),
                getTagSet("lead")),
            new Person(new Name("Hannah Teo"), new Phone("97654321"), new Email("hannah.teo@gmail.com"),
                new Role("Analyst"),
                new Department("Finance"),
                getTagSet("mid-level")),
            new Person(new Name("Isaac Koh"), new Phone("91567890"), new Email("isaac.koh@outlook.com"),
                new Role("Engineer"),
                new Department("Technology"),
                getTagSet("senior")),
            new Person(new Name("Jolene Yeo"), new Phone("93456789"), new Email("jolene.yeo@yahoo.com"),
                new Role("Coordinator"),
                new Department("Operations"),
                getTagSet("mid-level")),
            new Person(new Name("Kenny Ng"), new Phone("91345678"), new Email("kenny.ng@gmail.com"),
                new Role("Recruiter"),
                new Department("Human Resources"),
                getTagSet("junior")),
            new Person(new Name("Lydia Soh"), new Phone("98765432"), new Email("lydia.soh@outlook.com"),
                new Role("Manager"),
                new Department("Sales"),
                getTagSet("senior")),
            new Person(new Name("Marcus Tan"), new Phone("91234890"), new Email("marcus.tan@gmail.com"),
                new Role("Specialist"),
                new Department("Legal"),
                getTagSet("mid-level")),
            new Person(new Name("Nadia Rahman"), new Phone("92345678"), new Email("nadia.rahman@yahoo.com"),
                new Role("Executive"),
                new Department("Marketing"),
                getTagSet("senior")),
            new Person(new Name("Owen Lim"), new Phone("91876543"), new Email("owen.lim@gmail.com"),
                new Role("Analyst"),
                new Department("Finance"),
                getTagSet("senior")),
            new Person(new Name("Peyton Tan"), new Phone("94567890"), new Email("peyton.tan@outlook.com"),
                new Role("Coordinator"),
                new Department("Operations"),
                getTagSet("senior")),
            new Person(new Name("Quinn Lee"), new Phone("91654321"), new Email("quinn.lee@gmail.com"),
                new Role("Engineer"),
                new Department("Technology"),
                getTagSet("junior")),
            new Person(new Name("Rachel Ong"), new Phone("99876543"), new Email("rachel.ong@yahoo.com"),
                new Role("Recruiter"),
                new Department("Human Resources"),
                getTagSet("senior")),
            new Person(new Name("Samuel Goh"), new Phone("91523456"), new Email("samuel.goh@gmail.com"),
                new Role("Manager"),
                new Department("Sales"),
                getTagSet("mid-level")),
            new Person(new Name("Tessa Lim"), new Phone("96789012"), new Email("tessa.lim@outlook.com"),
                new Role("Specialist"),
                new Department("Legal"),
                getTagSet("junior")),
            new Person(new Name("Uma Chia"), new Phone("91098765"), new Email("uma.chia@gmail.com"),
                new Role("Executive"),
                new Department("Marketing"),
                getTagSet("mid-level")),
            new Person(new Name("Victor Koh"), new Phone("97891234"), new Email("victor.koh@yahoo.com"),
                new Role("Analyst"),
                new Department("Finance"),
                getTagSet("lead")),
            new Person(new Name("Wendy Teo"), new Phone("91432109"), new Email("wendy.teo@gmail.com"),
                new Role("Engineer"),
                new Department("Technology"),
                getTagSet("lead")),
            new Person(new Name("Xavier Ng"), new Phone("92109876"), new Email("xavier.ng@outlook.com"),
                new Role("Coordinator"),
                new Department("Operations"),
                getTagSet("lead")),
            new Person(new Name("Yasmin Noor"), new Phone("98234567"), new Email("yasmin.noor@gmail.com"),
                new Role("Recruiter"),
                new Department("Human Resources"),
                getTagSet("lead")),
            new Person(new Name("Zachary Soh"), new Phone("91234506"), new Email("zachary.soh@yahoo.com"),
                new Role("Manager"),
                new Department("Sales"),
                getTagSet("junior")),
            new Person(new Name("Amelia Yeo"), new Phone("93210987"), new Email("amelia.yeo@gmail.com"),
                new Role("Specialist"),
                new Department("Legal"),
                getTagSet("lead")),
            new Person(new Name("Brandon Tan"), new Phone("91567234"), new Email("brandon.tan@outlook.com"),
                new Role("Executive"),
                new Department("Marketing"),
                getTagSet("junior")),
            new Person(new Name("Chloe Rahman"), new Phone("96012345"), new Email("chloe.rahman@gmail.com"),
                new Role("Analyst"),
                new Department("Finance"),
                getTagSet("mid-level")),
            new Person(new Name("Dylan Lim"), new Phone("91890123"), new Email("dylan.lim@yahoo.com"),
                new Role("Engineer"),
                new Department("Technology"),
                getTagSet("mid-level"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
