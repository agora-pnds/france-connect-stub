package fr.gouv.franceconnect.stub;

import fr.gouv.franceconnect.stub.exceptions.InvalidConfigurationException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.Scanner;

import static fr.gouv.franceconnect.stub.ConfigUtil.CONF;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Utility class to get user data.
 *
 * @author asirko
 * @author tchabaud
 *
 * */
enum UserEnum {

    TEST("test@test.fr", "test");

    private final static String SECRET = CONF.get("stub.franceconnect.config.oidc.clientsecret");
    private final static String CLIENT_ID = CONF.get("stub.franceconnect.config.oidc.clientid");

    private static final Key KEY = new SecretKey() {

        /**
         * Le serialVersionUID.
         */
        private static final long serialVersionUID = 1505050299784801589L;

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
            return SECRET.getBytes();
        }
    };

    private final String email;

    private final String id;

    UserEnum(final String email, final String id) {
        this.email = email;
        this.id = id;
    }

    private static UserEnum getUserForToken(final String email) {
        for (final UserEnum user : UserEnum.values()) {
            if (user.email.equals(email)) {
                return user;
            }
        }
        return null;
    }

    public static boolean userExists(final String email) {
        return getUserForToken(email) != null;
    }

    public static String getToken(final String email, String nonce, long expirationTime) {
        JwtBuilder builder = Jwts.builder();

        builder.setSubject(Base64Codec.BASE64.encode(email));
        builder.setAudience(CLIENT_ID);
        builder.setExpiration(new Date(expirationTime));
        builder.setIssuedAt(new Date());
        builder.setIssuer(CONF.get("stub.franceconnect.config.oidc.issuer"));
        builder.claim("nonce", nonce);

        return builder.signWith(SignatureAlgorithm.HS256, KEY).compact();
    }

    public static String getJsonForToken(final String token) {
        // For this stub, token == email
        final UserEnum user = getUserForToken(token);
        final String userId = (user != null) ? user.id : "";

        final String jsonDirPropName = "dir.stub";
        final String jsonDir = System.getProperty(jsonDirPropName);
        if (StringUtils.isNotBlank(jsonDir)) {
            throw new InvalidConfigurationException("System property "
                    + jsonDirPropName + " is not set or empty !");
        }

        final File jsonDirPath = new File(jsonDir);
        if (!jsonDirPath.isDirectory()) {
            throw new InvalidConfigurationException("System property "
                    + jsonDirPropName + " does not point to a directory !");
        }

        final String filePath = jsonDirPath.getAbsolutePath()
                + File.separatorChar + userId + ".json";
        final File file = new File(filePath);

        final StringBuilder result = new StringBuilder();
        try (Scanner scanner = new Scanner(file, UTF_8.displayName())) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                result.append(line).append('\n');
            }
            scanner.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
