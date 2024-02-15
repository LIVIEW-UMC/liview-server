package umc.liview.exception.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import umc.liview.common.utils.logger.RequestLogger;
import umc.liview.common.utils.logger.ResponseLogger;
import umc.liview.exception.BusinessException;
import umc.liview.exception.ErrorResponse;
import umc.liview.exception.code.ErrorCode;

import javax.naming.SizeLimitExceededException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 예외 처리시 발생
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(HttpServletRequest request, BusinessException e) {
        return createErrorResponse(request, e, e.getErrorCode());
    }

    // javax.validation.Valid or @Validated 으로 binding error 발생시 발생
    // HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> methodArgumentValidation(HttpServletRequest request, MethodArgumentNotValidException e) {
        return createErrorResponse(request, e, ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    // @ModelAttribute 으로 바인딩 에러시 발생
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> bindException(HttpServletRequest request, BindException e) {
        return createErrorResponse(request, e, ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    // 지원하지 않은 HTTP Method 호출 할 경우 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> requestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        return createErrorResponse(request, e, ErrorCode.INVALID_METHOD_TYPE);
    }

    // JSON 형식 지키지 않았을 시 발생
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> invalidHttpMessageParsing(HttpServletRequest request, HttpMessageNotReadableException e) {
        return createErrorResponse(request, e, ErrorCode.INVALID_JSON_TYPE);
    }

    // 데이터 잘못 넘어갔을 경우 발생
    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ErrorResponse> illegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        return createErrorResponse(request, e, ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    // 데이터 무결성 위반한 경우 발생
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> dataIntegrityViolationException(HttpServletRequest request, DataIntegrityViolationException e) {
        return createErrorResponse(request, e, ErrorCode.DATA_INTEGRITY_VIOLATE);
    }

    // 이미지 크기 초과시 발생
    @ExceptionHandler({MaxUploadSizeExceededException.class, SizeLimitExceededException.class, MissingServletRequestPartException.class, MultipartException.class})
    protected ResponseEntity<ErrorResponse> imageFileSizeExceedException(HttpServletRequest request, Exception e) {
        return createErrorResponse(request, e, ErrorCode.FILE_SIZE);
    }

    // 나머지 에러 여기서 핸들링
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
        return createErrorResponse(request, e, ErrorCode.SERVICE_UNAVAILABLE);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpServletRequest request, Exception e, ErrorCode errorCode) {
        // Create ExceptionResponse
        ResponseEntity<ErrorResponse> response;
        if (e.getClass().equals(MethodArgumentNotValidException.class)) {
            response = ErrorResponse.toResponseEntity(ErrorCode.INVALID_REQUEST_PARAMETER,
                    ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" and ")));
        } else {
            response = ErrorResponse.toResponseEntity(errorCode);
        }

        // Logging And Return
        RequestLogger.logging(request);
        ResponseLogger.loggingFailedResponse(response, e);
        return response;
    }
}
