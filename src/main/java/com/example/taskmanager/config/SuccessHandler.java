package com.example.taskmanager.config;

import com.example.taskmanager.models.User;
import com.example.taskmanager.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SuccessHandler implements AuthenticationSuccessHandler {
    private final HttpSession httpSession;
    private final UserRepository userRepository;
    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String username;
        if(authentication.getPrincipal() instanceof DefaultOAuth2User userDetails){
            username = userDetails.getAttribute("email");
            User user = userRepository.findByEmail(username).orElse(new User());
            if(user.getRoles().isEmpty()){
                new DefaultRedirectStrategy().sendRedirect(request,response,"/registration?oauthEmail="+username);
                return;
            }
        } else {
            username = authentication.getName();
        }
        httpSession.setAttribute(username,username);
        new DefaultRedirectStrategy().sendRedirect(request,response,"/home/"+username);
    }
}
