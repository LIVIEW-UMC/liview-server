package umc.liview.common.utils.logger;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@UtilityClass
public class RequestLogger {

    // Request
    public static void logging(HttpServletRequest request) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(getLoggingStructure());
        logBuilder.append(getRequestURI(request)).append("\n");
        logBuilder.append("[Request Headers] : ").append(parsingHeaders(request)).append("\n");
        logBuilder.append("[Request Body] : ").append("\n").append(parsingBody(request));
        log.info(logBuilder.toString());
    }

    // Avoid Multipart Request
    public static void loggingMultipartRequest(HttpServletRequest request) {
        StringBuilder logBuilder = new StringBuilder();
        logBuilder.append(getLoggingStructure());
        logBuilder.append(getRequestURI(request)).append("\n");
        logBuilder.append("[Request Headers] : ").append(parsingHeaders(request)).append("\n");
        logBuilder.append("[Request Body] : This request includes Multipart Files").append("\n");
        log.info(logBuilder.toString());
    }

    // Logging Requested URI
    private static String getRequestURI(HttpServletRequest request) {
        String httpMethod = "[HTTP Method] : " + request.getMethod();
        String requestURI = "[Request URI] : " + request.getRequestURI();

        return httpMethod + "\n" + requestURI;
    }

    // Logging Requested Headers
    private static Map<String, Object> parsingHeaders(HttpServletRequest request) {
        Map<String, Object> headerMap = new HashMap<>();

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headerName = headers.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    // Logging Content of RequestBody
    private static String parsingBody(HttpServletRequest request) {
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;

        if (request != null) {
            byte[] buf = cachingRequest.getContentAsByteArray();
            if (buf.length > 0) {
                try {
                    return new String(buf, 0, buf.length, cachingRequest.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    return " Unsupported Encoding ";
                }
            }
        }
        return "EMPTY BODY ";
    }

    public static String getLoggingStructure() {
        return "\n" + "[Title] : Successful Requested Information" + "\n";
    }
}
