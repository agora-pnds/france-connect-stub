/**
 * 
 */
package name.chabs.france.connect.stub;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.Scanner;

/**
 * @author c82asir
 * 
 */
public enum UserEnum {

    TEST("test@test.fr", "test");

    private static final Key KEY = MacProvider.generateKey();

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

    public static String getToken(final String email) {
        return Jwts.builder().setSubject(email).signWith(SignatureAlgorithm.HS512, KEY).compact();
    }

    public static String getJsonForToken(final String token) {
        final String email = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token).getBody().getSubject();
        final UserEnum user = getUserForToken(email);

        final ClassLoader classLoader = UserEnum.class.getClassLoader();
        final File file = new File(classLoader.getResource("json/" + user.id + ".json").getFile());

        final StringBuilder result = new StringBuilder("");
        try (Scanner scanner = new Scanner(file)) {
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
