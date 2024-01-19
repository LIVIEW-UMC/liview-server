package umc.liview.exception.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TourCode {

    NOT_FOUND("T-001"),
    ;

    private final String code;
}
