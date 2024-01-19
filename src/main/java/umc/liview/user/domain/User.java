package umc.liview.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import umc.liview.common.BaseTimeEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "second_name")
    private String secondName;
    @Column(unique = true, name = "email")
    private String email;
    @Column(name = "introduction")
    private String introduction;
    @Column(name = "password")
    private String password;
    @Embedded
    private PrivacyStatus privacyStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status")
    private ActivationStatus activationStatus;


    @Getter
    @RequiredArgsConstructor
    public enum ActivationStatus {
        ACTIVATED("활성화"),
        INACTIVATED("비활성화");

        private final String status;
    }
}
