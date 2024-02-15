package umc.liview.auth.presentation;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.liview.auth.application.AuthService;
import umc.liview.auth.presentation.dto.UserDtoMapper;
import umc.liview.auth.presentation.dto.request.TokenReissueDto;
import umc.liview.auth.presentation.dto.response.TokenResponse;
import umc.liview.config.auth.JwtUserDetails;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserDtoMapper mapper;

    @GetMapping("/{socialType}/login")
    public void oauth2Login(HttpServletResponse response, @PathVariable(name = "socialType") String socialLoginPath) throws IOException {
        response.sendRedirect("https://jin-myserver.shop/oauth2/authorization/" + socialLoginPath);
    }

    @GetMapping("/login-success")
    public TokenResponse login(@RequestParam String access_token, @RequestParam String refresh_token) {
        return new TokenResponse(access_token, refresh_token);
    }


    @PostMapping("/reissue")
    public TokenResponse reissue(@Valid @RequestBody TokenReissueDto tokenReissueDto) {
        return authService.reissue(mapper.toCommand(tokenReissueDto));
    }

    @PostMapping("/logout")
    public void logout(@AuthenticationPrincipal JwtUserDetails userDetails) {
        authService.logout(userDetails.getUserId());
    }
}
