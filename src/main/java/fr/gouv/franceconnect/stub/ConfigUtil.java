package fr.gouv.franceconnect.stub;

import fr.gouv.franceconnect.stub.exceptions.InvalidConfigurationException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by tchabaud on 06/01/2017.
 * Configuration singleton.
 */

enum ConfigUtil {
    CONF;

    private static final String CONF_PROPERTY = "config.properties";

    /**
     * Configuration holder.
     */
    private final Properties config;

    ConfigUtil() {
        config = loadConfiguration();
    }

    private Properties loadConfiguration() {
        final Properties config = new Properties();
        final String configFilePath = System.getProperty(CONF_PROPERTY);
        if (configFilePath == null || "".equals(configFilePath.trim())) {
            throw new InvalidConfigurationException("La propriété d'environnement " + CONF_PROPERTY
                    + " n'a pas été définie !");
        }
        final File configFile = new File(configFilePath);
        try (final FileInputStream inputStream = new FileInputStream(configFile)) {
            config.load(inputStream);
        } catch (NullPointerException | IOException e) {
            throw new InvalidConfigurationException("Configuration introuvable. Vérifier la valeur de la propriété "
                    + CONF_PROPERTY, e);
        }

        return config;
    }

    /**
     * Read again configuration file and refresh the current configuration.
     */
    public void refresh() {
        final Properties newConf = loadConfiguration();
        config.clear();
        config.putAll(newConf);
    }

    /**
     * @return the current configuration
     */
    public Properties get() {
        return config;
    }

    /**
     * @param pKey : property key
     * @return the property value
     */
    public String get(String pKey) {
        return config.getProperty(pKey);
    }
}
