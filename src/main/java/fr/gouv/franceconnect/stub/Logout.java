package fr.gouv.franceconnect.stub;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author asirko
 * 
 */
public class Logout extends HttpServlet {

    /** */
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {
      //Do nothing
        
      String redirectUriParam = req.getParameter("post_logout_redirect_uri");
      
      final URI redirectUri;
      try {
          redirectUri = new URI(redirectUriParam);
      } catch (final URISyntaxException e) {
          throw new ServletException(e);
      }
      
      resp.setContentType("Content-type: text/html");
      resp.sendRedirect(redirectUri.toString());
      
      
    }
}
