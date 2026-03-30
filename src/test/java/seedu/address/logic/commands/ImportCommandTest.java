package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ImportCommandTest {
    @Test
    void equals_samePath_returnsTrue() {
        assertTrue(new ImportCommand("/a/b.csv").equals(new ImportCommand("/a/b.csv")));
    }

    @Test
    void equals_differentPath_returnsFalse() {
        assertNotEquals(new ImportCommand("/a/b.csv"), new ImportCommand("/x/y.csv"));
    }

    @Test
    void equals_sameObject_returnsTrue() {
        ImportCommand cmd = new ImportCommand("/a/b.csv");
        assertEquals(cmd, cmd);
    }

    @Test
    void equals_differentType_returnsFalse() {
        assertNotEquals(new ImportCommand("/a/b.csv"), "not a command");
    }

    @Test
    void getConfirmationPrompt_containsActionAndImpactSummary() {
        ImportCommand cmd = new ImportCommand("/some/path.csv");
        String prompt = cmd.getConfirmationPrompt();
        assertTrue(prompt.contains(ImportCommand.ACTION_SUMMARY));
        assertTrue(prompt.contains(ImportCommand.IMPACT_SUMMARY));
    }

    @Test
    void getActionDescription_returnsExpectedString() {
        assertEquals(ImportCommand.ACTION_DESCRIPTION,
            new ImportCommand("/some/path.csv").getActionDescription());
    }

}
