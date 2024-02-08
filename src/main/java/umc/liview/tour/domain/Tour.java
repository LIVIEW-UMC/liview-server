package umc.liview.tour.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.*;
import umc.liview.community.Post;
import umc.liview.tour.dto.TourRequestDTO;
import umc.liview.user.domain.Folder;
import umc.liview.user.domain.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Tour extends Serializers.Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "contents")
    private String contents;
//    @Column(name = "is_completed") 없어도 될 거 같은데 ?
//    private boolean isCompleted;
    @Column(name = "is_classified") //얜 뭐하는 애지 ?
    private boolean isClassified;

    @Embedded
    private Period period;


    @Enumerated(EnumType.STRING)
    @Column(name = "complete_status")
    private Tour.CompleteStatus completeStatus;

    @Getter
    @RequiredArgsConstructor
    public enum CompleteStatus {
        COMPLETE("완성"),
        INCOMPLETE("미완성"),
        ;

        private final String completeStatus;
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Transient
    private List<TourImages> tourImages = new ArrayList<>();

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourTags> tourTags = new ArrayList<>();



    public static Tour toTourEntity(TourRequestDTO tourRequestDTO){
        return Tour.builder()
                .completeStatus(tourRequestDTO.getCompleteStatus())
                .contents(tourRequestDTO.getContents())
                .title(tourRequestDTO.getTitle())
                .build();

    }


}
