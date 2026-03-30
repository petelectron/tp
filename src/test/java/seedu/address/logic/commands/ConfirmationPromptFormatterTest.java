package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ConfirmationPromptFormatterTest {

    @Test
    public void format_nullActionSummary_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ConfirmationPromptFormatter.format(null, "impact"));
    }

    @Test
    public void format_nullImpactSummary_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ConfirmationPromptFormatter.format("action", null));
    }

    @Test
    public void format_validArgs_returnsFormattedString() {
        assertNotNull(ConfirmationPromptFormatter.format("action", "impact"));
    }
}

