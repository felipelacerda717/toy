package loja.toystore.toy.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        
        request.getSession().setAttribute("message", "Login realizado com sucesso!");
        request.getSession().setAttribute("alertClass", "alert-success");

        if (roles.contains("ROLE_ADMIN")) {
            response.sendRedirect("/products/admin");
        } else {
            String targetUrl = determineTargetUrl(request);
            response.sendRedirect(targetUrl);
        }
    }

    private String determineTargetUrl(HttpServletRequest request) {
        String targetUrl = request.getParameter("targetUrl");
        if (targetUrl != null && !targetUrl.isEmpty()) {
            return targetUrl;
        }
        return "/checkout"; // Default URL for regular users
    }
}