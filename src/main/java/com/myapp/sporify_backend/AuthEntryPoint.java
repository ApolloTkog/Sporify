package com.myapp.sporify_backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Η κλάση που καλείται όταν υπάρχει κάποιο Error στο authentication
 * Είναι το entry point καλείται στην αρχή και επιστρέφει κάποιο error
 * με UNAUTHORIZED 401 και μήνυμα ~ Maybe you have to login first ~
 */

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        logger.error("Unauthorized error: {}", authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Maybe you have to login first");
    }

}
