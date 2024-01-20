package umc.liview.tour.domain;

import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.liview.community.Post;
import umc.liview.user.domain.User;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Tour extends Serializers.Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "contents")
    private String contents;
    @Column(name = "is_completed")
    private boolean isCompleted;
    @Column(name = "is_classified")
    private boolean isClassified;
    @Embedded
    private Period period;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    @OneToMany(mappedBy = "tour")
    private List<TourImages> tourImages = new ArrayList<>();
    @OneToMany(mappedBy = "tour")
    private List<TourTags> tourTags = new ArrayList<>();
}
