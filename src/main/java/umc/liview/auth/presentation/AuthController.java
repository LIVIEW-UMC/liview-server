package umc.liview.auth.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import umc.liview.auth.application.AuthService;
import umc.liview.auth.presentation.dto.UserDtoMapper;
import umc.liview.auth.presentation.dto.request.TokenReissueDto;
import umc.liview.auth.presentation.dto.response.TokenResponse;
import umc.liview.config.auth.JwtUserDetails;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserDtoMapper mapper;

    @PostMapping("/reissue")
    public TokenResponse reissue(@Valid @RequestBody TokenReissueDto tokenReissueDto) {
        return authService.reissue(mapper.toCommand(tokenReissueDto));
    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal JwtUserDetails userDetails) {
        authService.logout(userDetails.getUserId());
    }
}
