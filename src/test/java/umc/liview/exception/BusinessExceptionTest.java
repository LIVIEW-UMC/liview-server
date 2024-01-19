package umc.liview.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import umc.liview.exception.code.ErrorCode;

import java.util.ArrayList;

@SpringBootTest
class BusinessExceptionTest {

    void throwException() {
        ArrayList<String> lists = new ArrayList<>();
        lists.add("울산");
        lists.add("부산");
        lists.add("경주");

        if (!lists.contains("대구"))
            throw new BusinessException(ErrorCode.TOUR_NOT_FOUND);
    }

    @Test
    void 비즈니스_예외처리_테스트() {

        // given
        try {
            // when
            throwException();
        } catch (BusinessException e) {
            // then
            Assertions.assertEquals(ErrorCode.TOUR_NOT_FOUND, e.getErrorCode());

        }
    }
}
