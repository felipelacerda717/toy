package loja.toystore.toy.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class LogoutSuccessListener implements ApplicationListener<LogoutSuccessEvent> {

    @Override
    public void onApplicationEvent(LogoutSuccessEvent event) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        HttpSession session = request.getSession(true);
        session.setAttribute("message", "Logout realizado com sucesso!");
        session.setAttribute("alertClass", "alert-info");
    }
}