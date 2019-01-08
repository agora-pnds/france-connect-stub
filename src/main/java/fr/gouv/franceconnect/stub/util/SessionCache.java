package fr.gouv.franceconnect.stub.util;

import fr.gouv.franceconnect.stub.exceptions.CacheException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum SessionCache {
    CACHE;

    private final Map<String, String> cache;

    private final Logger logger = LoggerFactory.getLogger(SessionCache.class);

    SessionCache() {
        cache = loadCache();
    }

    private ConcurrentHashMap<String, String> loadCache() {
        logger.info("New empty nonces cache created.");
        return new ConcurrentHashMap<>();
    }

    /**
     * @param key : key for value to retrieve from cache.
     * @return value associated to key from cache.
     */
    public String get(final String key) {
        if (StringUtils.isBlank(key)) {
            throw new CacheException("key can't be null or empty !");
        }

        long matchingKey = Long.MAX_VALUE;
        for (String currentKey : cache.keySet()) {
            if (currentKey.startsWith(key)) {
                final long timeStamp = Long.parseLong(currentKey.replace(key, ""));
                matchingKey = (matchingKey > timeStamp) ? timeStamp : matchingKey;
            }
        }

        final String timedKey = key + matchingKey;
        final String value = cache.get(timedKey);
        logger.debug("Nonce value {} fetched from cache for key {}", value, timedKey);
        // Remove nonce, one shot use
        final String removedValue = cache.remove(timedKey);
        logger.debug("Nonce value {} removed from nonces cache for key {}", removedValue, timedKey);
        return value;
    }

    /**
     * Store new value for key @key in cache.
     *
     * @param key   : Key to store in cache.
     * @param value : value to store.
     */
    public void put(final String key, final String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            throw new CacheException("key / value can't be null or empty !");
        }

        final long timeStamp = System.nanoTime();
        final String timedKey = key + timeStamp;
        final String exists = cache.put(timedKey, value);
        logger.debug("New value {} inserted for key {}", value, timedKey);
        if (exists != null) {
            throw new CacheException("A value already exists in cache for the key " + key);
        }
    }
}
