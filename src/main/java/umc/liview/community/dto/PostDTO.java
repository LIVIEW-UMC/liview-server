package umc.liview.community.dto;

import com.amazonaws.services.ec2.model.transform.PurchaseReservedInstancesOfferingResultStaxUnmarshaller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.liview.community.domain.Post;
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
    private int likeCount;
    private int commentCount;

    @Builder
    public PostDTO(Long tourId,String title,String imageURL,String startDate,String endDate,int viewCount,int likeCount){
            this.tourId=tourId;
            this.title=title;
            this.imageURL = imageURL;
            this.startDate = startDate;
            this.endDate = endDate;
            this.viewCount = viewCount;
            this.commentCount = 0;
            this.likeCount = likeCount;
    }

    public static PostDTO toPostDTO(Tour tour, String imageURL, Post post){

        return PostDTO.builder()
                .endDate(tour.getEndDay())
                .title(tour.getTitle())
                .imageURL(imageURL)
                .tourId(tour.getId())
                .startDate(tour.getStartDay())
                .viewCount(post.getViewCounts())
                .likeCount(post.getLikes().size())
                .build();
    }
}
