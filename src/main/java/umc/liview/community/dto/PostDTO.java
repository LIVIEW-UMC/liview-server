package umc.liview.community.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class PostDTO {
    private Long tourId;
    private String title;
    private String imageURL;
    private String startDate;
    private String endDate;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;
}
