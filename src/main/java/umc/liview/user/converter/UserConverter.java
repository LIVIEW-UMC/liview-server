package umc.liview.user.converter;

import umc.liview.user.domain.User;
import umc.liview.user.web.dto.UserRequestDTO;
import umc.liview.user.web.dto.UserResponseDTO;

public class UserConverter {
    public static UserResponseDTO.SimpleProfile toSimpleProfile(User user, Long follower, Long following){
        return UserResponseDTO.SimpleProfile.builder()
                .name(user.getFirstName()+user.getSecondName())
                .email(user.getEmail())
                .follower(follower)
                .following(following)
                .build();
    }

    public static UserResponseDTO.UserProfile toUserProfile(User user){
        return UserResponseDTO.UserProfile.builder()
                .firstName(user.getFirstName())
                .secondName(user.getSecondName())
                .introduction(user.getIntroduction())
                .email(user.getEmail())
                .path("Not path")
                .build();
    }

    public static User toUser(UserRequestDTO.PutUserProfile userProfile){
        return User.builder()
                .firstName(userProfile.getFirstName())
                .secondName(userProfile.getSecondName())
                .introduction(userProfile.getIntroduction())
                .email(userProfile.getEmail())
                .build();
    }

    public static UserResponseDTO.UserPrivacy toUserPrivacy(User user){
        return UserResponseDTO.UserPrivacy.builder()
                .isInActivated(user.getPrivacyStatus().isInActivated())
                .toursAutoPosted(user.getPrivacyStatus().isToursAutoPosted())
                .build();
    }
}
