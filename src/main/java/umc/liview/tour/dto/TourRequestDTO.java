package umc.liview.tour.dto;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.domain.TourImages;

import java.util.List;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TourRequestDTO {
 //태그추가해야함
    private String title;
    private String contents;
    private Tour.CompleteStatus completeStatus;
    private List<String> hashtag;
    private String isClassfied;
    private Long tourId;

}
