package fr.gouv.franceconnect.stub.api;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.gouv.franceconnect.stub.util.CacheUtil;

/**
 * @author asirko
 */
public class Userinfo extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        final String bearer = req.getHeader("Authorization");
        // Header should contains 'Bearer <token>'
        final String token = URLDecoder.decode(bearer.split(" ")[1], UTF_8.displayName());
        final String json = CacheUtil.getJsonForToken(token);
        resp.setContentType("application/json; charset=" + UTF_8.displayName().toLowerCase());
        final PrintWriter out = new PrintWriter(new OutputStreamWriter(
                resp.getOutputStream(), UTF_8), true);
        out.print(json);
        out.flush();
    }
}
