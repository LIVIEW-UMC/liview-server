package umc.liview.user.converter;

import umc.liview.user.domain.User;
import umc.liview.user.dto.UserResponseDTO;

public class UserConverter {
    public static UserResponseDTO.SimpleProfile toSimpleProfile(User user, Long follower, Long following){
        return UserResponseDTO.SimpleProfile.builder()
                .name(user.getName())
                .email(user.getEmail())
                .follower(follower)
                .following(following)
                .build();
    }

    public static UserResponseDTO.UserProfile toUserProfile(User user){
        return UserResponseDTO.UserProfile.builder()
                .name(user.getName())
                .introduction(user.getIntroduction())
                .email(user.getEmail())
                .path("Not path")
                .build();
    }

    public static UserResponseDTO.UserPrivacy toUserPrivacy(User user){
        return UserResponseDTO.UserPrivacy.builder()
                .isInActivated(user.getPrivacyStatus().isEmailReceptionStatus())
                .toursAutoPosted(user.getPrivacyStatus().isToursAutoPosted())
                .build();
    }
}
