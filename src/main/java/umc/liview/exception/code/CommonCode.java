package umc.liview.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonCode {
    REQUEST_PARAMETER("C-001"),
    JSON_TYPE("C-002"),
    METHOD_NOT_ALLOWED("C-003"),
    FILE_SIZE("C-004"),
    SERVICE_UNAVAILABLE("C-005"),
    UNAUTHORIZED_OPERATION("C-006"),
    DATA_INTEGRITY("C-007"),
    TOKEN_VERIFY("C-008")
    ;

    private final String code;
}
