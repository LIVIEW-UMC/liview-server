package umc.liview.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import umc.liview.common.basetime.BaseTimeEntity;
import umc.liview.community.domain.Comments;
import umc.liview.tour.domain.Tour;
import umc.liview.user.dto.UserRequestDTO;

import java.util.ArrayList;
import java.util.List;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(unique = true, name = "email")
    private String email;
    @Column(name = "introduction")
    private String introduction;
    @Column(name = "image_url")
    private String imgUrl;
    @Embedded
    private PrivacyStatus privacyStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Folder> folders = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Follow> follows = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Tour> tours = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Comments> comments = new ArrayList<>();

    @Builder
    public User(String name, String email, String imgUrl) {
        this.name = name;
        this.email = email;
        this.imgUrl = imgUrl;
        introduction = "";
        privacyStatus = new PrivacyStatus();
        role = Role.USER;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Role {
        USER("ROLE_USER"),
        ADMIN("ROLE_ADMIN");

        private final String value;
    }

    public void modifyUserProfile(UserRequestDTO.PutUserProfile userProfile){
        this.introduction = userProfile.getIntroduction();
    }

    public void modifyUserProfile(UserRequestDTO.PutUserProfile userProfile, String imgUrl){
        this.imgUrl = imgUrl;
        this.introduction = userProfile.getIntroduction();
    }
}
