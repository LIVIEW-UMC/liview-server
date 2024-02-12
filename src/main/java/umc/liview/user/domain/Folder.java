package umc.liview.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import umc.liview.common.basetime.BaseTimeEntity;
import umc.liview.tour.domain.TourTags;
import umc.liview.user.dto.FolderDTO;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Folder extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name",unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "owner")
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "folder" ,cascade = CascadeType.ALL)
    private List<StoredTours> storedTours = new ArrayList<>();


    @Getter
    @RequiredArgsConstructor
    public enum Owner {
        MINE("나의_일정"),
        OTHERS("다른_회원_일정"),
        ;
        private final String ownerStatus;
    }

    public Folder changeName(String name){
        this.name = name;
        return this;
    }





}
