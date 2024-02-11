package umc.liview.user.dto;

import lombok.Getter;

public class UserRequestDTO {

    @Getter
    public static class UserId {
        Long userId;
    }
    @Getter
    public static class PutUserProfile {
        Long userId;
        String Name;
        String email;
        String introduction;
    }
}