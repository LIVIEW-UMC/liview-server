package umc.liview.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Component
public class CachingRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Response Wrapping
        ContentCachingResponseWrapper wrappingResponse = new ContentCachingResponseWrapper(response);

        // Multipart Type 이면 Skip
        if (verifyMultipartFileIncluded(request)) {
            filterChain.doFilter(request, wrappingResponse);
            return;
        }

        // Request Wrapping
        ContentCachingRequestWrapper wrappingRequest = new ContentCachingRequestWrapper(request);
        filterChain.doFilter(wrappingRequest, wrappingResponse);
        wrappingResponse.copyBodyToResponse();
    }

    private boolean verifyMultipartFileIncluded(HttpServletRequest request) {
        if (request.getContentType() != null && request.getContentType().contains("multipart")) {
            request.setAttribute("isMultipartFile", true);
            return true;
        } else {
            request.setAttribute("isMultipartFile", false);
            return false;
        }
    }
}
