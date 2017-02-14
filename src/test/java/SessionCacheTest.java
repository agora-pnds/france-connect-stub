import static fr.gouv.franceconnect.stub.util.SessionCache.CACHE;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import fr.gouv.franceconnect.stub.exceptions.CacheException;

/**
 * Unit test class for {@link fr.gouv.franceconnect.stub.util.SessionCache}.
 */
public class SessionCacheTest {

    private static final String EMAIL = "firstname%2blastname%40email.com";
    private final Logger logger = LoggerFactory.getLogger(SessionCacheTest.class);

    @BeforeSuite
    public void beforeSuite() {
        logger.info("Init Cache {} ", CACHE.name());
    }

    @Test(expectedExceptions = CacheException.class)
    public void testStoreEmptyKey() {
        CACHE.put(null, "");
    }

    @Test(expectedExceptions = CacheException.class)
    public void testStoreEmptyValue() {
        CACHE.put(EMAIL, null);
    }

    @Test(expectedExceptions = CacheException.class)
    public void testGetEmptyKey() {
        CACHE.get("");
    }

    @Test
    public void testNonceCache() {
        final ConcurrentLinkedQueue<String> valueCache = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < 99; i++) {
            final String nonce = RandomStringUtils.randomAlphabetic(14);
            valueCache.add(nonce);
            logger.info("Value {} added for index {}", nonce, i);
            CACHE.put(EMAIL, nonce);
        }

        for (int idx = 0; idx < valueCache.size(); idx++) {
            final String expectedValue = valueCache.remove();
            logger.info("Value {} fetched from queue.", expectedValue);
            final String fetchedValue = CACHE.get(EMAIL);
            Assert.assertEquals(expectedValue, fetchedValue);
        }
    }
}
