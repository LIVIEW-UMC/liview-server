package umc.liview.community.dto;

import com.amazonaws.services.ec2.model.transform.PurchaseReservedInstancesOfferingResultStaxUnmarshaller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.liview.tour.domain.Tour;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostDTO {
    private Long tourId;
    private String title;
    private String imageURL;
    private String startDate;
    private String endDate;
    private int viewCount;
    private Long likeCount;
    private Long commentCount;

    @Builder
    public PostDTO(Long tourId,String title,String imageURL,String startDate,String endDate,int viewCount){
            this.tourId=tourId;
            this.title=title;
            this.imageURL = imageURL;
            this.startDate = startDate;
            this.endDate = endDate;
            this.viewCount = viewCount;
            this.commentCount = 0L;
            this.likeCount = 0L;
    }

    public static PostDTO toPostDTO(Tour tour,String imageURL,int viewCount){


        return PostDTO.builder()
                .endDate(tour.getEndDay())
                .title(tour.getTitle())
                .imageURL(imageURL)
                .tourId(tour.getId())
                .startDate(tour.getStartDay())
                .viewCount(viewCount)
                .build();
    }
}
