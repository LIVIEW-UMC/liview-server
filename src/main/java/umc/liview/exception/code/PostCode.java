package umc.liview.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostCode {

    POST_NOT_FOUND("P-001"),
    COMMENTS_NOT_FOUND("P-002"),
    REPLY_NOT_FOUND("P-003"),
    ;

    private final String code;
}
