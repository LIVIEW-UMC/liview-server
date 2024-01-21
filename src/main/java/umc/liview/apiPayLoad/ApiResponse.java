package umc.liview.apiPayLoad;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;


/*
       🌟 ***isSuccess*** : **성공인지 아닌지 알려주는 필드**입니다.

        ***code*** : HTTP 상태코드로는 너무 제한적인 정보만 줄 수 있어서 **조금 더 세부적인 응답 상황을 알려주기 위한 필드**입니다.

        ***message*** : **code에 추가적으로 우리에게 익숙한 문자로 상황을 알려주는 필드**입니다.

        ***result*** : **실제로 클라이언트에게 필요한 데이터**가 담깁니다. 보통 에러 상황에는 null을 담지만, 9주차에서 null을 담지 않는 에러 상황도 간단히 다루고 10주차에 제대로 다룰 것입니다

*/
@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message","result"})
public class ApiResponse<T> {
    @JsonProperty("isSuccess")
    private final Boolean isSucess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;



}
