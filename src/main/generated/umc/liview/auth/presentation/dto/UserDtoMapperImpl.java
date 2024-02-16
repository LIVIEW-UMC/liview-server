package umc.liview.auth.presentation.dto;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import umc.liview.auth.application.dto.TokenReissueCommand;
import umc.liview.auth.presentation.dto.request.TokenReissueDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-02-16T18:18:55+0900",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 17.0.5 (JetBrains s.r.o.)"
)
@Component
public class UserDtoMapperImpl implements UserDtoMapper {

    @Override
    public TokenReissueCommand toCommand(TokenReissueDto tokenReissueDto) {
        if ( tokenReissueDto == null ) {
            return null;
        }

        String accessToken = null;
        String refreshToken = null;

        accessToken = tokenReissueDto.getAccessToken();
        refreshToken = tokenReissueDto.getRefreshToken();

        TokenReissueCommand tokenReissueCommand = new TokenReissueCommand( accessToken, refreshToken );

        return tokenReissueCommand;
    }
}
