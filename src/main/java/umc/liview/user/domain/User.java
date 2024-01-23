package umc.liview.user.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.liview.common.BaseTimeEntity;
import umc.liview.community.Comments;
import umc.liview.tour.domain.Tour;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
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

    @OneToMany(mappedBy = "user")
    private List<Folder> folders = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Follow> follows = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Tour> tours = new ArrayList<>();
    @OneToMany(mappedBy = "user")
    private List<Comments> comments = new ArrayList<>();

    @Getter
    @RequiredArgsConstructor
    public enum ActivationStatus {
        ACTIVATED("활성화"),
        INACTIVATED("비활성화"),
        ;


        private final String status;
    }


    public void toggleActivationStatus(){
        if(this.getActivationStatus() == ActivationStatus.ACTIVATED){
            this.activationStatus = ActivationStatus.INACTIVATED;
        }
        else{
            this.activationStatus = ActivationStatus.ACTIVATED;
        }
    }
}
