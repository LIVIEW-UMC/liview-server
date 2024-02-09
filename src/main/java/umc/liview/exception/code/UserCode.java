package umc.liview.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserCode {

    NOT_FOUND("U-001"),
    ;

    private final String code;
}
