package fr.gouv.franceconnect.stub;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created by tchabaud on 15/10/16.
 * Stub for France Connect authorize endpoint.
 */
public class Authorize extends HttpServlet {

    /** */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
        req.getSession().setAttribute("state", req.getParameter("state"));
        req.getSession().setAttribute("redirect_uri", req.getParameter("redirect_uri"));
        req.getSession().setAttribute("client_id", req.getParameter("client_id"));
        req.getSession().setAttribute("nonce", req.getParameter("nonce"));
        
        
        final String scope = URLEncoder.encode(req.getParameter("scope"), "UTF-8");
        final String response_type = URLEncoder.encode(req.getParameter("response_type"), "UTF-8");
        final String nonce = URLEncoder.encode(req.getParameter("nonce"), "UTF-8");
        final String redirect_uri = URLEncoder.encode(req.getParameter("redirect_uri"), "UTF-8");
        final String state = URLEncoder.encode(req.getParameter("state"), "UTF-8");
        final String client_id = URLEncoder.encode(req.getParameter("client_id"), "UTF-8");

        final StringBuilder uri = new StringBuilder("/index.jsp").append("?") //
                .append("scope").append('=').append(scope) //
                .append("&response_type").append('=').append(response_type) //
                .append("&nonce").append('=').append(nonce) //
                .append("&redirect_uri").append('=').append(redirect_uri) //
                .append("&state").append('=').append(state) //
                .append("&client_id").append('=').append(client_id);
        final URI redirectUri;
        try {
            redirectUri = new URI(uri.toString());
        } catch (final URISyntaxException e) {
            throw new ServletException(e);
        }
        resp.setContentType("Content-type: text/html");
        resp.sendRedirect(redirectUri.toString());

        //resp.sendRedirect("/index.jsp");
    }
}
