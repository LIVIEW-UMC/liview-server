package umc.liview.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import umc.liview.config.jwt.JwtException;
import umc.liview.exception.code.ErrorCode;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final String detail;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getStatus().value())
                        .error(errorCode.getStatus().name())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String message) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getStatus().value())
                        .error(errorCode.getStatus().name())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .detail(message)
                        .build());
    }

    public static ResponseEntity<ErrorResponse> toResponseEntity(JwtException jwtException) {
        return ResponseEntity
                .status(jwtException.getStatus())
                .body(ErrorResponse.builder()
                        .status(jwtException.getStatus().value())
                        .error(jwtException.getStatus().name())
                        .code(jwtException.getCode())
                        .message(jwtException.getMessage())
                        .build());
    }

    @Override
    public String toString() {
        return "{\n" +
                "    \"timestamp\":" + timestamp + ",\n" +
                "    \"status\":" + status + ",\n" +
                "    \"error\":\"" + error + "\",\n" +
                "    \"code\":\"" + code + "\",\n" +
                "    \"message\":\"" + message + "\"\n" +
                "}";
    }
}
