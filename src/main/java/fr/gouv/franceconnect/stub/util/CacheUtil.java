package fr.gouv.franceconnect.stub.util;

import static fr.gouv.franceconnect.stub.util.ConfigUtil.CONF;
import static fr.gouv.franceconnect.stub.util.UsersJsonCache.USER_CACHE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * @author tchabaud
 *         Utility class for cache accessors
 */
public class CacheUtil {

    private static final Logger logger = LoggerFactory.getLogger(CacheUtil.class);

    private static final Key KEY = new SecretKey() {
        /**
         * Le serialVersionUID.
         */
        private static final long serialVersionUID = 1L;

        @Override
        public String getAlgorithm() {
            return "HS256";
        }

        @Override
        public String getFormat() {
            return "HMACSHA256";
        }

        @Override
        public byte[] getEncoded() {
            return CONF.get(ConfigUtil.CLIENT_SECRET).getBytes(UTF_8);
        }
    };

    /**
     * @param f : json file to parse
     * @return email value extracted from json file f
     */
    static String getEmailFromJson(File f) throws IOException {
        @SuppressWarnings("unchecked") HashMap<String, Object> result = (HashMap<String, Object>) new ObjectMapper().readValue(f, HashMap.class);
        return getJsonValue("email", result);
    }

    /**
     * @return true if user email is found in nonces
     */
    public static boolean userExists(final String email) {
        return USER_CACHE.users().containsKey(email);
    }

    public static String getToken(final String email, String nonce, long expirationTime) throws IOException {
        JwtBuilder builder = Jwts.builder();
        @SuppressWarnings("unchecked") final HashMap<String, Object> data = (HashMap<String, Object>) new ObjectMapper().readValue(
                USER_CACHE.users().get(email), HashMap.class);
        builder.setSubject(getJsonValue("sub", data));
        builder.setAudience(CONF.get(ConfigUtil.CLIENT_ID));
        builder.setExpiration(new Date(expirationTime));
        builder.setIssuedAt(new Date());
        builder.setIssuer(CONF.get(ConfigUtil.ISSUER));
        builder.claim("nonce", nonce);
        return builder.signWith(SignatureAlgorithm.HS256, KEY).compact();
    }

    /**
     * @param jsonData Data to parse
     * @param key      Key to find
     * @return String value corresponding to key in jsonData.
     */
    private static String getJsonValue(final String key, final HashMap<String, Object> jsonData) {
        for (Object keyObject : jsonData.keySet()) {
            final String currentKey = (String) keyObject;
            if (key.equals(currentKey)) {
                return (String) jsonData.get(key);
            }
        }
        return "";
    }

    /**
     * @param f : file to read
     * @return data byte array corresponding to file f.
     * @throws IOException if problem accessing file f.
     */
    static byte[] readData(final File f) throws IOException {
        final Path filePath = Paths.get(f.getAbsolutePath());
        final byte[] data = Files.readAllBytes(filePath);
        logger.info("Loaded data from file {}", f.getAbsolutePath());
        return data;
    }

    /**
     * @param token : token used for cache search.
     * @return json data corresponding to token.
     */
    public static String getJsonForToken(final String token) {
        return USER_CACHE.users().get(token);
    }
}
