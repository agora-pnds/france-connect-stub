package fr.gouv.franceconnect.stub.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.gouv.franceconnect.stub.exceptions.InvalidConfigurationException;

/**
 * Utility class to store Json stubs
 *
 * @author asirko
 * @author tchabaud
 */
public enum UsersJsonCache {
    USER_CACHE;

    private final ConcurrentHashMap<String, String> jsonCache;

    private final Logger logger = LoggerFactory.getLogger(UsersJsonCache.class);

    UsersJsonCache() {
        try {
            jsonCache = loadCache();
        } catch (IOException e) {
            logger.error("Json cache initialization failed !");
            throw new RuntimeException(e);
        }
    }

    private ConcurrentHashMap<String, String> loadCache() throws IOException {
        final ConcurrentHashMap<String, String> userCache = new ConcurrentHashMap<>();

        File jsonDir = null;
        try {
            jsonDir = ConfigUtil.getJsonDir();
        } catch (URISyntaxException e) {
            logger.error("Specified stub directory can't be found !");
        }

        if (jsonDir == null || !jsonDir.isDirectory()) {
            throw new InvalidConfigurationException("File " + jsonDir + " is not a directory !");
        }
        final File[] jsonDirectory = jsonDir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getAbsolutePath().endsWith(".json");
            }
        });
        if (jsonDirectory != null) {
            for (File f : jsonDirectory) {
                final String email = CacheUtil.getEmailFromJson(f);
                if (StringUtils.isBlank(email)) {
                    logger.error("Email not found in file {}", f.getAbsolutePath());
                } else {
                    try {
                        final byte[] data = CacheUtil.readData(f);
                        userCache.put(email, new String(data, UTF_8.displayName()));
                    } catch (IOException e) {
                        logger.error("No data read from file {}", f.getAbsolutePath());
                    }
                }
            }
        }
        return userCache;
    }

    /**
     * @return the current users nonces
     */
    public ConcurrentHashMap<String, String> users() {
        return jsonCache;
    }
}
