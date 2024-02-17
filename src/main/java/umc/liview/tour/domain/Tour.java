package umc.liview.tour.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import umc.liview.common.basetime.BaseTimeEntity;
import umc.liview.community.domain.Post;
import umc.liview.tour.dto.TourRequestDTO;
import umc.liview.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@ToString
public class Tour extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "contents")
    private String contents;
    @Column(name = "size")
    private String size;
    @Column(name = "is_classified") //얜 뭐하는 애지 ?
    private boolean isClassified;
    @Column
    private String startDay;
    @Column
    private String endDay;
    @Enumerated(EnumType.STRING)
    @Column(name = "complete_status")
    private CompleteStatus completeStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;
    @JsonManagedReference
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourImages> tourImages = new ArrayList<>();
    @JsonIgnore
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private List<TourTags> tourTags = new ArrayList<>();

    public static Tour toTourEntity(TourRequestDTO tourRequestDTO){
        return Tour.builder()
                .completeStatus(tourRequestDTO.getCompleteStatus())
                .contents(tourRequestDTO.getContents())
                .title(tourRequestDTO.getTitle())
                .isClassified(Boolean.parseBoolean(tourRequestDTO.getIsClassfied()))
                .size(tourRequestDTO.getSize())
                .startDay(tourRequestDTO.getStartDay())
                .endDay(tourRequestDTO.getEndDay())
                .build();

    }

    @Getter
    @RequiredArgsConstructor
    public enum CompleteStatus {
        COMPLETE("완성"),
        INCOMPLETE("미완성"),
        ;

        private final String completeStatus;
    }

    public void setPost(Post post){
        this.post = post;
    }
    public void setUser(User user){
        this.user = user;
    }

    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String contents){
        this.contents = contents;
    }

    public void changeCompleteStatus(Tour.CompleteStatus status){
        this.completeStatus = status;
    }

    public void changeClassfied(boolean bool){
        this.isClassified = bool;
    }

    public void changeStartDay(String day){
        this.startDay = day;
    }

    public void changeEndDay(String day){
        this.endDay = day;
    }

    public void changeSize(String size){
        this.size = size;
    }
}
