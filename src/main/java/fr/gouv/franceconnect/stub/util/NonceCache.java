package fr.gouv.franceconnect.stub.util;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum NonceCache {
    NONCE_CACHE;

    private final ConcurrentHashMap<String, String> cache;

    private final Logger logger = LoggerFactory.getLogger(UsersJsonCache.class);

    NonceCache() {
        cache = loadCache();
    }

    private ConcurrentHashMap<String, String> loadCache() {
        logger.info("New empty nonces cache created.");
        return new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, String> nonces() {
        return cache;
    }
}
