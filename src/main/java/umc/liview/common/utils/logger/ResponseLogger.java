package umc.liview.common.utils.logger;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import umc.liview.config.jwt.JwtException;
import umc.liview.exception.ErrorResponse;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class ResponseLogger {

    // Successful Response
    public void loggingSuccessResponse(HttpServletResponse response) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(getLoggingStructure());
        logBuilder.append("[Response Status] : ").append(getStatus(response)).append("\n");
        logBuilder.append("[Request Headers] : ").append(parsingHeaders(response)).append("\n");
        log.info(logBuilder.toString());
    }

    // Failed Response With Exception
    public void loggingFailedResponse(ResponseEntity<ErrorResponse> response, Exception ex) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(getExceptionHandlingLoggingStructure());
        logBuilder.append("[Exception Class] : ").append(getExceptionName(ex)).append("\n");
        logBuilder.append("[Exception Message] : ").append(parsingExceptionMessage(ex)).append("\n");
        logBuilder.append("[Response Body With Exception] : ").append("\n").append(response.getBody());
        log.warn(logBuilder.toString());
    }

    // Failed Response With JWT-Exception
    public void loggingWithJWTExceptionInfo(HttpServletRequest request, ResponseEntity<ErrorResponse> response, JwtException ex) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(getExceptionHandlingLoggingStructure());
        logBuilder.append(getRequestURI(request)).append("\n");
        logBuilder.append("[JWT Exception Class] : ").append(JwtException.valueOf(ex.name())).append("\n");
        logBuilder.append("[JWT Exception Message] : ").append(ex.getMessage()).append("\n");
        logBuilder.append("[Response Body With Exception] : ").append("\n").append(response.getBody());
        log.warn(logBuilder.toString());
    }

    // Logging Response Status
    private String getStatus(HttpServletResponse response) {
        HttpStatus responseStatus = HttpStatus.valueOf(response.getStatus());
        return responseStatus.value() + " - " +
                responseStatus.getReasonPhrase();
    }

    // Logging Response Headers
    private Map<String, Object> parsingHeaders(HttpServletResponse response) {
        Map<String, Object> headerMap = new HashMap<>();

        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            headerMap.put(headerName, response.getHeader(headerName));
        }
        return headerMap;
    }

    // Logging Exception Class Name
    private String getExceptionName(Exception e) {
        return e.getClass().getSimpleName();
    }

    // Logging Exception Message
    private String parsingExceptionMessage(Exception e) {
        String message = e.getMessage();
        if (e.getClass().equals(MethodArgumentNotValidException.class)) {
            message = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" and "));
        }
        if (message == null) {
            message = "EMPTY MESSAGE";
        }
        return message;
    }

    // Logging Requested URI
    private String getRequestURI(HttpServletRequest request) {
        String httpMethod = "[HTTP Method] : " + request.getMethod();
        String requestURI = "[Request URI] : " + request.getRequestURI();
        return httpMethod + "\n" + requestURI;
    }

    public String getLoggingStructure() {
        return """

                [Title] : Successful Responsing Information
                """;
    }

    public String getExceptionHandlingLoggingStructure() {
        return """

                [Title] : Handling Exception Information
                """;
    }
}
