package umc.liview.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDTO {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class SimpleProfile {
        String path;
        String name;
        String email;
        Long follower;
        Long following;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UserProfile {
        String path;
        String name;
        String email;
        String introduction;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class UserPrivacy {
        boolean isInActivated;
        boolean toursAutoPosted;
    }
}