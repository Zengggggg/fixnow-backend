package com.fixnow.backend.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class CustomFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        if (exception instanceof DisabledException) {
            setDefaultFailureUrl("/login?error=disabled");
        } else if (exception instanceof BadCredentialsException) {
            setDefaultFailureUrl("/login?error=bad_credentials");
        } else {
            setDefaultFailureUrl("/login?error=unknown");
        }

        super.onAuthenticationFailure(request, response, exception);
    }
}
