package umc.liview.exception;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import umc.liview.exception.code.ErrorCode;

import java.util.ArrayList;

@Slf4j
@SpringBootTest
class ExceptionTest {

    void throwBusinessException_Test() {
        ArrayList<String> lists = new ArrayList<>();
        lists.add("울산");
        lists.add("부산");
        lists.add("경주");

        if (!lists.contains("대구"))
            throw new BusinessException(ErrorCode.TOUR_NOT_FOUND);
    }

    void throwNotFoundException_Test() {
        ArrayList<String> lists = new ArrayList<>();
        lists.add("울산");
        lists.add("부산");
        lists.add("경주");

        if (!lists.contains("대구"))
            throw new NotFoundException(ErrorCode.TOUR_NOT_FOUND, 2);
    }

    @Test
    void 비즈니스_예외처리_테스트() {
        // given
        try {
            // when
            throwBusinessException_Test();
        } catch (BusinessException e) {
            // then
            Assertions.assertEquals(ErrorCode.TOUR_NOT_FOUND, e.getErrorCode());

        }
    }

    @Test
    void NOT_FOUND_예외처리_테스트() {
        // given
        try {
            // when
            throwNotFoundException_Test();
        } catch (NotFoundException e) {
            // then
            log.warn(e.getMessage());
            Assertions.assertEquals(ErrorCode.TOUR_NOT_FOUND, e.getErrorCode());
        }
    }

//    @Test
//    void 날짜_테스트() {
//        log.info(String.valueOf("20240301"  "20240228"));
//    }
}
