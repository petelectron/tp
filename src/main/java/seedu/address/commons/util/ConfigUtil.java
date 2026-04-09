package seedu.address.commons.util;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.core.Config;
import seedu.address.commons.exceptions.DataLoadingException;

/**
 * A class for accessing the Config File.
 */
public class ConfigUtil {

    /**
     * Returns the configuration stored at the specified file path.
     *
     * @param configFilePath Path to the configuration file.
     * @return Configuration data from the specified file, if present.
     * @throws DataLoadingException If the configuration data cannot be loaded.
     */
    public static Optional<Config> readConfig(Path configFilePath) throws DataLoadingException {
        return JsonUtil.readJsonFile(configFilePath, Config.class);
    }

    /**
     * Saves the specified configuration to the given file path.
     *
     * @param config Configuration to save.
     * @param configFilePath Path to the configuration file.
     * @throws IOException If the configuration data cannot be saved.
     */
    public static void saveConfig(Config config, Path configFilePath) throws IOException {
        JsonUtil.saveJsonFile(config, configFilePath);
    }

}
