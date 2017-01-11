package fr.gouv.franceconnect.stub.api;

import static fr.gouv.franceconnect.stub.util.NonceCache.NONCE_CACHE;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.gouv.franceconnect.stub.util.CacheUtil;

/**
 * Stub for Token France Connect endpoint.
 *
 * @author dgombert
 * @author tchabaud
 */
public class Token extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(Token.class);

    /*
     * Access token generation
     */
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        final String email = req.getParameter("code");
        final String nonce = NONCE_CACHE.nonces().get(URLEncoder.encode(email, UTF_8.displayName()));

        if (nonce == null || nonce.trim().length() == 0) {
            throw new IllegalArgumentException("No nonce found for this session.");
        }

        if (!CacheUtil.userExists(email)) {
            throw new RuntimeException("No matching user found in json stubs.");
        }

        // Expiration date
        long expirationTime = new Date().getTime() + 360000;

        final String json = "{\"access_token\":\"" + email + "\", \"expires_in\":" + expirationTime
                + ", \"token_type\":\"Bearer\", \"id_token\":\"" + CacheUtil.getToken(email, nonce, expirationTime) + "\"}";

        resp.setContentType("application/json");
        resp.setCharacterEncoding(UTF_8.displayName());
        final PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();

        // Remove nonce from nonces, one shot use
        NONCE_CACHE.nonces().remove(URLEncoder.encode(email, UTF_8.displayName()));
        logger.info("Nonce removed from nonces for user {}", email);
    }
}
