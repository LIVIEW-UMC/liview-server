package umc.liview.user.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umc.liview.user.domain.PrivacyStatus;
import umc.liview.user.domain.User;


@Getter
@Setter
@NoArgsConstructor // 기본생성자
@AllArgsConstructor // 모든 필드를 매개변수로 하는 생성자

public class UserDTO {


    private long id;
    private String firstName;
    private String secondName;
    private String email;
    private String introduction;
    private PrivacyStatus privacyStatus;
    private User.ActivationStatus activationStatus;

    public static UserDTO toUserDTO(User user) {

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setIntroduction(user.getIntroduction());
        userDTO.setSecondName(user.getSecondName());
        userDTO.setPrivacyStatus(user.getPrivacyStatus());
        userDTO.setActivationStatus(user.getActivationStatus());

        return userDTO;
    }

}