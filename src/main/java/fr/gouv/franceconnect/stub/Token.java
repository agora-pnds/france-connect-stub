package fr.gouv.franceconnect.stub;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Date;

/**
 * Created by tchabaud on 15/10/16.
 * Stub for Token France Connect endpoint.
 */
public class Token extends HttpServlet {

    /** */
    private static final long serialVersionUID = 1L;

    /*
     * Génération de l'acces token
     */
    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException,
            IOException {

       
        final String email = req.getParameter("code");
        final String nonce = CacheApplicatif.getInstance().get(URLEncoder.encode(email, "UTF-8"));
        
          
        if(nonce == null || nonce.trim().length() == 0){
        	throw new IllegalArgumentException("Aucun nonce trouve pour la session");
        }
        
        if (!UserEnum.userExists(email)) {
            throw new RuntimeException("pas d'utilisateur dans l'enum");
        }
        
        //Date d'expiration
        long expirationTime = new Date().getTime() + 360000;

        final String json = "{\"access_token\":\"" + email + "\", \"expires_in\":" + expirationTime
                + ", \"token_type\":\"Bearer\", \"id_token\":\"" + UserEnum.getToken(email, nonce, expirationTime) + "\"}";

        resp.setContentType("application/json");
        final PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
        
        //On vide le cache car utilisation unique
        CacheApplicatif.getInstance().remove(URLEncoder.encode(email, "UTF-8"));
    }
}
