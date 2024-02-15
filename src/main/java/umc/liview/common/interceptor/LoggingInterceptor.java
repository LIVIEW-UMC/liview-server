package umc.liview.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import umc.liview.common.utils.logger.RequestLogger;
import umc.liview.common.utils.logger.ResponseLogger;

@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

    // PostHandler's Logging - 성공하면 실행
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                            @org.springframework.lang.Nullable ModelAndView modelAndView) throws Exception {
        logRequest(request);
        logSuccessfulResponse(response);
    }

    private void logRequest(HttpServletRequest request) {
        RequestLogger.logging(request);
    }

    private void logSuccessfulResponse(HttpServletResponse response) {
        ResponseLogger.loggingSuccessResponse(response);
    }
}
