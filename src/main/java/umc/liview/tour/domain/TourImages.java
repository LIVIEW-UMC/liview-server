package umc.liview.tour.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import umc.liview.common.basetime.BaseTimeEntity;

import java.util.Date;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
@ToString
public class TourImages extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotNull
    @Column(name = "img_url")
    private String imageUrl;

    @Column(name = "img_location")  //위치명
    private String imageLocation;

    @Column(name= "date")
    private String date;

    @Column(name = "img_latitude")
    private double latitude;

    @Column(name = "img_longitude")
    private double longitude;

    @Column(name = "is_Thumbnail")
    private boolean isThumbnail;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY) //일정 삭제시 이미지도 자동으로 삭제 되도록
    @JoinColumn(name = "tour_id")
    private Tour tour;


}
