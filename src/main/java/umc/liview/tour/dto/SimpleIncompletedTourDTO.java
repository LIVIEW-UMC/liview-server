package umc.liview.tour.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.liview.tour.domain.TourImages;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SimpleIncompletedTourDTO {
    private Long tourId;
    private String title;
    private String imageURL;
    private LocalDateTime localDateTime;


}
