package umc.liview.tour.dto;

;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

@Getter
@NoArgsConstructor
@Builder
@Setter
@AllArgsConstructor
@ToString
public class ImageMetadataDTO{

    private String date;
    private String imgLocation;
    private double latitude;
    private double longitude;
    private String isThumbnail;


}

