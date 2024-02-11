package umc.liview.common.interceptor;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import umc.liview.common.utils.logger.RequestLogger;
import umc.liview.common.utils.logger.ResponseLogger;

@RequiredArgsConstructor
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) {

        // Request Logging
        if (!verifyMultipartFileContained(request)) {
            RequestLogger.logging(request);
        } else {
            RequestLogger.loggingMultipartRequest(request);
        }

        // Successful Response Logging
        if (isSuccess(response.getStatus())) {
            ResponseLogger.logging(response);
        }
    }

    private boolean verifyMultipartFileContained(HttpServletRequest request) {
        return (boolean) request.getAttribute("isMultipartFile");
    }

    private boolean isSuccess(int responseStatus) {
        return !HttpStatus.valueOf(responseStatus).is4xxClientError() && !HttpStatus.valueOf(responseStatus)
                .is5xxServerError();
    }
}
