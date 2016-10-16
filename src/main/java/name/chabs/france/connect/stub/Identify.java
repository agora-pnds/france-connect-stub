package name.chabs.france.connect.stub;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by tchabaud on 15/10/16.
 * Find stub content from e-mail input by user.
 */
public class Identify extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String targetUri = req.getParameter("redirect_uri");
        final String code = req.getParameter("code");
        final String nonce = req.getParameter("nonce");
        final String state = req.getParameter("state");
        final StringBuilder uri = new StringBuilder(targetUri)
                .append("?")
                .append("code").append('=').append(code)
                .append("nonce").append('=').append(nonce)
                .append("state").append('=').append(state);
        final URI redirectUri;
        try {
            redirectUri = new URI(uri.toString());
        } catch (URISyntaxException e) {
            throw new ServletException(e);
        }
        resp.setContentType("Content-type: text/html");
        resp.sendRedirect(redirectUri.toString());
    }
}
