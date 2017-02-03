package fr.gouv.franceconnect.stub.api;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.gouv.franceconnect.stub.util.CacheUtil;
import fr.gouv.franceconnect.stub.util.ConfigUtil;

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
        final String nonce = (String) req.getSession().getAttribute(ConfigUtil.NONCE_ATTR_NAME);

        // Remove nonce, one shot use
        req.getSession().removeAttribute(ConfigUtil.NONCE_ATTR_NAME);
        if (StringUtils.isBlank(nonce)) {
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

        logger.info("Nonce removed from nonces for user {}", email);
    }
}
