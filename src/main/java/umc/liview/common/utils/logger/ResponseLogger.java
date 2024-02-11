package umc.liview.common.utils.logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.util.ContentCachingResponseWrapper;
import umc.liview.config.jwt.JwtException;
import umc.liview.exception.ErrorResponse;

import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class ResponseLogger {

    // Successful Response
    public void logging(HttpServletResponse response) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(getLoggingStructure());
        logBuilder.append("[Response Body]").append("\n");
        logBuilder.append(parsingBody(response));
        log.info(logBuilder.toString());
    }

    // Failed Response With Exception
    public void loggingWithExceptionInfo(ResponseEntity<ErrorResponse> response, Exception ex) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(getExceptionHandlingLoggingStructure());
        logBuilder.append("[Exception Class] : ").append(getExceptionName(ex)).append("\n");
        logBuilder.append("[Exception Message] : ").append(parsingExceptionMessage(ex)).append("\n");
        logBuilder.append("[Response Body With Exception] : ").append("\n").append(response.getBody());
        log.warn(logBuilder.toString());
    }

    // Failed Response With JWT-Exception
    public void loggingWithJWTExceptionInfo(ResponseEntity<ErrorResponse> response, JwtException ex) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(getExceptionHandlingLoggingStructure());
        logBuilder.append("[JWT Exception Class] : ").append(ex.getClass().getSimpleName()).append("\n");
        logBuilder.append("[JWT Exception Message] : ").append(ex.getMessage()).append("\n");
        logBuilder.append("[Response Body With Exception] : ").append("\n").append(response.getBody());
        log.warn(logBuilder.toString());
    }

    // Logging Content of ResponseBody
    private String parsingBody(HttpServletResponse response) {
        final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;

        if (cachingResponse != null) {
            byte[] buf = cachingResponse.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Object json = objectMapper.readValue(buf, Object.class);
                    return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
                } catch (IOException e) {
                    return "Failed to parse response body";
                }
            }
        }
        return "EMPTY BODY ";
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
                    .map(err -> err.getDefaultMessage()).collect(Collectors.joining(" and "));
        }
        if (message == null) {
            message = "EMPTY MESSAGE";
        }
        return message;
    }

    public static String getLoggingStructure() {
        return "\n" + "[Title] : Successful Responsing Information" + "\n";
    }

    public String getExceptionHandlingLoggingStructure() {
        return "\n" + "[Title] : Handling Exception Information" + "\n";
    }
}
