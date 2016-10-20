package name.chabs.france.connect.stub;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by tchabaud on 15/10/16.
 * Stub for Token France Connect endpoint.
 */
public class Token extends HttpServlet {

    /** */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

        final String email = req.getParameter("code");

        if (!UserEnum.userExists(email)) {
            throw new RuntimeException("pas d'utilisateur dans l'enum");
        }

        final String json = "{\"access_token\":\"" + email + "\", \"expires_in\":" + new Date().getTime() + 3600
                + ", \"token_type\":\"Bearer\", \"id_token\":\"" + UserEnum.getToken(email) + "\"}";

        resp.setContentType("application/json");
        final PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
