package umc.liview.community.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.liview.common.basetime.BaseTimeEntity;
import umc.liview.tour.domain.Tour;
import umc.liview.user.domain.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "view_counts")
    private int viewCounts;
    @Enumerated(EnumType.STRING)
    @Column(name = "post_status")
    private PostStatus postStatus;

    @OneToOne(mappedBy = "post")
    private Tour tour;
    @OneToMany(mappedBy = "post")
    private List<Comments> comments = new ArrayList<>();
    @OneToMany(mappedBy = "post")
    private List<Likes> likes = new ArrayList<>();

    @Getter
    @RequiredArgsConstructor
    public enum PostStatus {
        PUBLIC("공개"),
        PRIVATE("비공개");

        private final String status;
    }


    public void toggleActivationStatus() {
        if (postStatus == PostStatus.PUBLIC) {
            postStatus = PostStatus.PRIVATE;
        } else {
            postStatus = PostStatus.PUBLIC;
        }
    }


    public void increaseViewCount(){
        this.viewCounts = this.getViewCounts() + 1;
    }
}
