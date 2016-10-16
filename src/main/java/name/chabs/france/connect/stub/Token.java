package name.chabs.france.connect.stub;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by tchabaud on 15/10/16.
 * Stub for Token France Connect endpoint.
 */
public class Token extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        my $jws_token = encode_jwt(payload=>$token, extra_headers=>{typ=>'JWT'}, alg=>'HS256', key=>'2222222222222222222222222222222222222222222222222222222222222222');
//        print "Content-type: application/json; charset=utf-8\n\n{'access_token':'$jws_token', 'token_type':'Bearer', 'expires_in':3600, 'id_token':'$jws_token'}\n";
        final String token = "{ \"aud\": \"++\", \"exp\":" + new Date() + 3600 + ", \"iat\":\""++"\", \"iss\":"++", \"sub\":\""++"\", \"idp\":\"FC\", \"nonce\":\""+nonce+"\"}";
    }
}
