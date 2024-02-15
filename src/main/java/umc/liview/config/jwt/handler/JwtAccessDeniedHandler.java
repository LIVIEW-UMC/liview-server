package umc.liview.config.jwt.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import umc.liview.common.utils.logger.ResponseLogger;
import umc.liview.exception.ErrorResponse;

import java.io.IOException;

import static umc.liview.config.jwt.JwtException.*;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        ACCESS_DENIED.setResponse(response);
        ResponseLogger.loggingWithJWTExceptionInfo(request, ErrorResponse.toResponseEntity(ACCESS_DENIED), ACCESS_DENIED);
    }
}
