package seedu.address.commons.exceptions;

/**
 * Signals that a CSV file could not be parsed into a valid list of persons.
 */
public class CsvParseException extends RuntimeException {
    public CsvParseException(String message) {
        super(message);
    }

    public CsvParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
