package example.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Component
public class RESTAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private RESTAuthenticationFailureHandler authenticationFailureHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        String uri = request.getRequestURI();
        uri = java.net.URLDecoder.decode(uri, "UTF-8");
        String parms = "";
        if (uri.indexOf("{") > -1) {
            parms = uri.substring(uri.indexOf("{"), uri.lastIndexOf("}") + 1);
            parms = parms.replaceAll("\"", "");
        }

        JsonObject obj = (JsonObject) new JsonParser().parse(parms);
        String user = obj.get("user").getAsString();
        String password = obj.get("password").getAsString();

        if (true) {
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, password,
                    AuthorityUtils.createAuthorityList("ROLE_USER"));
            context.setAuthentication(authentication);
            authenticationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
        } else {
            authenticationFailureHandler.onAuthenticationFailure(request, response, authException);
        }
    }
}