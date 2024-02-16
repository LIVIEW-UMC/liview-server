package umc.liview.community.controller.dto;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import umc.liview.community.controller.dto.request.PostedDurationDto;
import umc.liview.community.service.dto.request.PostedDurationQueryDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-16T18:18:55+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (JetBrains s.r.o.)"
)
@Component
public class PostDtoMapperImpl implements PostDtoMapper {

    @Override
    public PostedDurationQueryDto toCommand(PostedDurationDto postedDurationDto) {
        if ( postedDurationDto == null ) {
            return null;
        }

        String startDay = null;
        String endDay = null;

        startDay = postedDurationDto.getStartDay();
        endDay = postedDurationDto.getEndDay();

        PostedDurationQueryDto postedDurationQueryDto = new PostedDurationQueryDto( startDay, endDay );

        return postedDurationQueryDto;
    }
}
