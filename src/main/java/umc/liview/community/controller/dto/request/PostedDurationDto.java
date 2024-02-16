package umc.liview.community.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PostedDurationDto {
    @NotBlank(message = "startDay is null")
    String startDay;
    @NotBlank(message = "endDay is null")
    String endDay;
}
