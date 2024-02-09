package umc.liview.user.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDTO {

    @Getter
    public static class UserId {
        Long userId;
    }
    @Getter
    public static class PutUserProfile {
        Long userId;
        String firstName;
        String secondName;
        String email;
        String introduction;
    }
}
