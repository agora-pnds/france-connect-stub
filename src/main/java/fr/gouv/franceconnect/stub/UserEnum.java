/**
 * 
 */
package fr.gouv.franceconnect.stub;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.Base64Codec;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Scanner;

/**
 * @author c82asir
 * 
 */
public enum UserEnum {
		
    TEST("test@test.fr", "test"),
    MYRIAM("m.lebrun@gmail.com", "myriam");

    private final static String SECRET = "2222222222222222222222222222222222222222222222222222222222222222";
    private final static String CLIENT_ID = "1111111111111111111111111111111111111111111111111111111111111111";

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

    private String email;

    private String id;

    UserEnum(final String email, final String id) {
        this.email = email;
        this.id = id;
    }

    public static UserEnum getUserForToken(final String email) {
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
        builder.setIssuer("http://impots-franceconnect.fr");
        builder.claim("nonce", nonce);
    	
    	return builder.signWith(SignatureAlgorithm.HS256, KEY).compact();
    }

    public static String getJsonForToken(final String token) {
        //Dans le cas du bouchon le token == email
        final UserEnum user = getUserForToken(token);

        final ClassLoader classLoader = UserEnum.class.getClassLoader();
        final File file = new File(classLoader.getResource("json/" + user.id + ".json").getFile());

        final StringBuilder result = new StringBuilder("");
        try (Scanner scanner = new Scanner(file, StandardCharsets.UTF_8.displayName())) {
            while (scanner.hasNextLine()) {
                final String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return result.toString();
    }
}
