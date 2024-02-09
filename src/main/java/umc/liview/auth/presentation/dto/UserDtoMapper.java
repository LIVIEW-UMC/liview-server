package umc.liview.auth.presentation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import umc.liview.auth.application.dto.TokenReissueCommand;
import umc.liview.auth.presentation.dto.request.TokenReissueDto;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {
    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);
    TokenReissueCommand toCommand(TokenReissueDto tokenReissueDto);
}
