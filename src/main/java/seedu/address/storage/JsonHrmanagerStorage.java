package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyHrmanager;

/**
 * A class to access Hrmanager data stored as a json file on the hard disk.
 */
public class JsonHrmanagerStorage implements HrmanagerStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonHrmanagerStorage.class);

    private Path filePath;

    public JsonHrmanagerStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getHrmanagerFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyHrmanager> readHrmanager() throws DataLoadingException {
        return readHrmanager(filePath);
    }

    /**
     * Similar to {@link #readHrmanager()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    public Optional<ReadOnlyHrmanager> readHrmanager(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableHRmanager> jsonHrmanager = JsonUtil.readJsonFile(
                filePath, JsonSerializableHRmanager.class);
        if (!jsonHrmanager.isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(jsonHrmanager.get().toModelType());
        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveHrmanager(ReadOnlyHrmanager addressBook) throws IOException {
        saveHrmanager(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveHrmanager(ReadOnlyHrmanager)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveHrmanager(ReadOnlyHrmanager addressBook, Path filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);
        JsonUtil.saveJsonFile(new JsonSerializableHRmanager(addressBook), filePath);
    }

}
