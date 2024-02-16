package umc.liview.community.controller.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import umc.liview.community.controller.dto.request.PostedDurationDto;
import umc.liview.community.service.dto.request.PostedDurationQueryDto;

@Mapper(componentModel = "spring")
public interface PostDtoMapper {
    PostDtoMapper INSTANCE = Mappers.getMapper(PostDtoMapper.class);

    PostedDurationQueryDto toCommand(PostedDurationDto postedDurationDto);
}
