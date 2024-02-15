package umc.liview.config.jwt.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import umc.liview.common.utils.logger.ResponseLogger;
import umc.liview.config.jwt.JwtException;
import umc.liview.exception.ErrorResponse;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        JwtException jwtException = getJwtException(request);
        jwtException.setResponse(response);
        ResponseLogger.loggingWithJWTExceptionInfo(request, ErrorResponse.toResponseEntity(jwtException), jwtException);
    }

    private JwtException getJwtException(HttpServletRequest request) {
        String exception = (String) request.getAttribute("exception");
        return exception == null ? JwtException.EMPTY_TOKEN : JwtException.valueOf(exception);
    }
}
