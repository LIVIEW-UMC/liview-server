package umc.liview.community.controller.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostDtoMapper {
    PostDtoMapper INSTANCE = Mappers.getMapper(PostDtoMapper.class);
}
