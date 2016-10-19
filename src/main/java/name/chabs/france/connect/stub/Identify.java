package name.chabs.france.connect.stub;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tchabaud on 15/10/16.
 * Find stub content from e-mail input by user.
 */
public class Identify extends HttpServlet {

    /** */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        final String email = req.getParameter("email");
        final String state = (String) req.getSession().getAttribute("state");
        final String redirect_uri = (String) req.getSession().getAttribute("redirect_uri");

        final StringBuilder uri = new StringBuilder(redirect_uri);
        uri.append("?").append("code=").append(email).append("&state=").append(state);
        System.out.println(uri.toString());

        final URI redirectUri;
        try {
            redirectUri = new URI(uri.toString());
        } catch (final URISyntaxException e) {
            throw new ServletException(e);
        }
        req.getRequestDispatcher(redirectUri.toString());
    }
}
