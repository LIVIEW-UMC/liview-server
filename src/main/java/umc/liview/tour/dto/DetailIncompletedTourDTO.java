package umc.liview.tour.dto;

import lombok.Builder;
import lombok.Getter;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.domain.TourImages;
import umc.liview.user.domain.User;

import java.util.List;

@Getter
@Builder
public class DetailIncompletedTourDTO {

    private Long tourId;
    private String title;
    private String contents;
    private List<String> hashtag;
    private List<TourImages> imgList;


    public static DetailIncompletedTourDTO detailIncompletedTourDTO(Tour tour,List<String> hashtag,List<TourImages> tourImagesList){
        return DetailIncompletedTourDTO.builder()
                .tourId(tour.getId())
                .contents(tour.getContents())
                .title(tour.getTitle())
                .hashtag(hashtag)
                .imgList(tourImagesList)
                .build();
    }
}
