package umc.liview.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import umc.liview.common.log.RequestLogger;
import umc.liview.common.log.ResponseLogger;
import umc.liview.exception.BusinessException;
import umc.liview.exception.ErrorResponse;
import umc.liview.exception.code.ErrorCode;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 예외 처리시 발생
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
        return createErrorResponse(e, e.getErrorCode());
    }

    // javax.validation.Valid or @Validated 으로 binding error 발생시 발생
    // HttpMessageConverter 에서 등록한 HttpMessageConverter binding 못할경우 발생
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> methodArgumentValidation(MethodArgumentNotValidException e) {
        log.error("handleMethodArgumentNotValidException", e);
        return createErrorResponse(e, ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    // @ModelAttribute 으로 바인딩 에러시 발생
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> bindException(BindException e) {
        log.error("handleBindException", e);
        return createErrorResponse(e, ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    // 지원하지 않은 HTTP Method 호출 할 경우 발생
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> requestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("handleHttpRequestMethodNotSupportedException", e);
        return createErrorResponse(e, ErrorCode.INVALID_METHOD_TYPE);
    }

    // JSON 형식 지키지 않았을 시 발생
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponse> invalidHttpMessageParsing(HttpMessageNotReadableException e) {
        log.error("handleHttpMessageNotReadableException", e);
        return createErrorResponse(e, ErrorCode.INVALID_JSON_TYPE);
    }

    // 데이터 잘못 넘어갔을 경우 발생
    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ErrorResponse> illegalArgumentException(IllegalArgumentException e) {
        log.error("handleIllegalArgumentException", e);
        return createErrorResponse(e, ErrorCode.INVALID_REQUEST_PARAMETER);
    }

    // 데이터 무결성 위반한 경우 발생
    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<ErrorResponse> dataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("handleDataIntegrityViolationException", e);
        return createErrorResponse(e, ErrorCode.DATA_INTEGRITY_VIOLATE);
    }

    // 나머지 에러 여기서 핸들링
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleEntityNotFoundException", e);
        return createErrorResponse(e, ErrorCode.SERVICE_UNAVAILABLE);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(Exception e, ErrorCode errorCode) {
        // create ExceptionResponse
        ResponseEntity<ErrorResponse> response;
        if (e.getClass().equals(MethodArgumentNotValidException.class)) {
            response = ErrorResponse.toResponseEntity(ErrorCode.INVALID_REQUEST_PARAMETER,
                    Objects.requireNonNull(((MethodArgumentNotValidException) e).getBindingResult().getFieldError()).getDefaultMessage());
        } else {
            response = ErrorResponse.toResponseEntity(errorCode);
        }

        // Logging and Return
        ResponseLogger.loggingWithExceptionInfo(response, e);
        return response;
    }
}
