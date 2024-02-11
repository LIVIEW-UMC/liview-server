package umc.liview.config.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import umc.liview.auth.application.UserCommandService;
import umc.liview.auth.application.UserQueryService;
import umc.liview.config.jwt.factory.JwtFactory;
import umc.liview.user.domain.User;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtFactory jwtFactory;
    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        // OAuthUserConstants -> OAuth2User
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        // email 로 회원 찾기 -> 없으면 저장
        Optional<User> optionalUser = findUser(email);
        if (optionalUser.isPresent()) {
            redirect(request, response, optionalUser.get(), false);
        } else {
            redirect(request, response, createAndSaveUser(oAuth2User), true);
        }
    }

    private User createAndSaveUser(OAuth2User oAuth2User) {
        User user = createUser(oAuth2User);
        return userCommandService.saveOAuthUser(user);
    }

    private void redirect (HttpServletRequest request, HttpServletResponse response, User user, boolean isNewUser) throws IOException {
        String accessToken = jwtFactory.createAccessToken(user.getId());
        String refreshToken = jwtFactory.createRefreshToken(user.getId());
        String uri = createRedirectionURI(accessToken, refreshToken, user.getId(), isNewUser).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private URI createRedirectionURI(String accessToken, String refreshToken, Long userId, boolean isNewUser) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("user_id", String.valueOf(userId));
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);
        queryParams.add("isNewUser", String.valueOf(isNewUser));

//         .host("http://localhost:3000/loginSuccess")

        //13.124.86.122
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .path("/loginSuccess")
                .queryParams(queryParams)
                .build()
                .toUri();
    }

    private User createUser(OAuth2User oAuth2User) {
        return User.builder()
                .name(oAuth2User.getAttribute("name"))
                .email(oAuth2User.getAttribute("email"))
                .imgUrl(oAuth2User.getAttribute("imageUrl"))
                .build();
    }

    private Optional<User> findUser(String email) {
        return userQueryService.findUserForOAuth(email);
    }
}
