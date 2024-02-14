package umc.liview.user.dto;

import lombok.Getter;

public class UserRequestDTO {
    @Getter
    public static class PutUserProfile {
        String introduction;
    }
}