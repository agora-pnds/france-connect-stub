package fr.gouv.franceconnect.stub;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author asirko
 */
public class Userinfo extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException,
            IOException {
        final String bearer = req.getHeader("Authorization");
        //le header contient 'Bearer <token>'
        final String token = bearer.split(" ")[1];

        final String json = UserEnum.getJsonForToken(token);

        resp.setCharacterEncoding(StandardCharsets.UTF_8.displayName());
        resp.setContentType("application/json");
        final PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
