package fr.gouv.franceconnect.stub.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.gouv.franceconnect.stub.exceptions.InvalidConfigurationException;

/**
 * Created by tchabaud on 06/01/2017.
 * Configuration singleton.
 */

public enum ConfigUtil {
    CONF;

    // Configuration props names
    public static final String CLIENT_SECRET = "stub.franceconnect.config.oidc.clientsecret";
    public static final String CLIENT_ID = "stub.franceconnect.config.oidc.clientid";
    public static final String ISSUER = "stub.franceconnect.config.oidc.issuer";
    private static final String CONF_PROPERTY = "/config.properties";
    // System property used to define json stub directory
    private static final String JSON_DIR = "dir.stub";
    private final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

    /**
     * Configuration holder.
     */
    private final Properties config;

    ConfigUtil() {
        config = loadConfiguration();
    }

    /**
     * @return Directory specified by {@link ConfigUtil} JSON_DIR system property if specified,
     * embedded json directory otherwise.
     */
    static File getJsonDir() throws URISyntaxException {
        final File file;
        if (StringUtils.isBlank(System.getProperty(ConfigUtil.JSON_DIR))) {
            final URL url = UsersJsonCache.class.getResource("/json");
            file = new File(url.toURI());
        } else {
            file = new File(System.getProperty(ConfigUtil.JSON_DIR));
        }

        return file;
    }

    private Properties loadConfiguration() {
        final Properties cfg = new Properties();
        final InputStream inputStream = getClass().getResourceAsStream(CONF_PROPERTY);
        try {
            cfg.load(inputStream);
            logger.info("Loading configuration from {} file ...", CONF_PROPERTY);
            for (String property : cfg.stringPropertyNames()) {
                final String value = cfg.getProperty(property);
                logger.info("{}={}", property, value);
            }
            logger.info("Configuration loaded");
        } catch (NullPointerException | IOException e) {
            throw new InvalidConfigurationException("Unable to find configuration in classpath /"
                    + CONF_PROPERTY, e);
        }
        return cfg;
    }

    /**
     * @param pKey : property key
     * @return the property value
     */
    public String get(String pKey) {
        return config.getProperty(pKey);
    }

}
