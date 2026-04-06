package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

/**
 * Utility class to build consistent confirmation prompts for destructive commands.
 */
public final class ConfirmationPromptFormatter {

    // Note: The window is exactly 3 lines big;
    // users may not realize the existence of the last line cut off
    // which can only be read by scrolling down
    private static final String PROMPT_TEMPLATE = "⚠ Warning! ⚠ Please confirm this action. "
            + "Enter 'y' to proceed or 'n' to cancel.%n"
            + "Action: %s%n"
            + "Impact: %s%n";

    private ConfirmationPromptFormatter() {}

    /**
     * Returns a readable, multi-line confirmation prompt.
     *
     * @param actionSummary Short description of the action that will be executed.
     * @param impactSummary Description of what will happen if the action proceeds.
     */
    public static String format(String actionSummary, String impactSummary) {
        requireNonNull(actionSummary);
        requireNonNull(impactSummary);
        return String.format(PROMPT_TEMPLATE, actionSummary, impactSummary);
    }
}
