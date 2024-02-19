package umc.liview.community.service.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import umc.liview.common.utils.formatter.TimeFormatter;
import umc.liview.community.service.dto.request.SearchLogCommand;

@Mapper(componentModel = "spring")
public interface PostCommandMapper {
    PostCommandMapper INSTANCE = Mappers.getMapper(PostCommandMapper.class);

    default SearchLogCommand toCommand(String name) {
        return new SearchLogCommand(name, TimeFormatter.calculateCurrentTimes());
    }
}
