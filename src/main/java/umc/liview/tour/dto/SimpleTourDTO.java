package umc.liview.tour.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SimpleTourDTO {
    private Long tourId;
    private String title;
    private String imageURL;
    private LocalDateTime localDateTime;
    private String size;

}
