package fr.gouv.franceconnect.stub.api;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fr.gouv.franceconnect.stub.util.ConfigUtil;

/**
 * @author tchabaud
 * Find stub content from e-mail input by user.
 */
public class Identify extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        final String email = URLEncoder.encode(req.getParameter("email"), UTF_8.displayName());
        final String state = (String) req.getSession().getAttribute("state");
        final String redirect_uri = (String) req.getSession().getAttribute("redirect_uri");

        final StringBuilder uri = new StringBuilder(redirect_uri);

        if (StringUtils.isBlank(redirect_uri)) {
            throw new ServletException("No redirect_uri provided !");
        }

        if (redirect_uri.contains("?")) {
            uri.append("&");
        } else {
            uri.append("?");
        }
        uri.append("code=").append(email).append("&state=").append(state);

        final URI redirectUri;
        try {
            redirectUri = new URI(uri.toString());
        } catch (final URISyntaxException e) {
            throw new ServletException(e);
        }
        
        resp.setContentType("Content-type: text/html");
        resp.sendRedirect(redirectUri.toString());

        // Store email <-> nonce association
        final String nonce = (String) req.getSession().getAttribute("nonce");
        if (StringUtils.isNotBlank(nonce)) {
            req.getSession().setAttribute(ConfigUtil.NONCE_ATTR_NAME, nonce);
        } else {
            throw new ServletException("No nonce provided !");
        }
    }
}
