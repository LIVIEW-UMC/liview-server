package umc.liview.config.oauth2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import umc.liview.config.auth.OAuthAttributes;

import java.util.Collections;
import java.util.Map;

import static umc.liview.user.domain.User.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthLoader implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 연동된 OAuth2.0 회원정보 가져오기
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // 연동된 소셜(Kakao, Naver 등) + 사용자 속성 가져오기
        String registrationId = getOAuthServiceName(userRequest);
        Map<String, Object> originAttributes = oAuth2User.getAttributes();

        // 소셜 서버에 등록된 회원 정보에서 필요한 정보만 -> OAuth2Attribute
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, originAttributes);
        Map<String, Object> memberAttribute = attributes.convertAttributesToMap();

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(Role.USER.getValue())),
                memberAttribute, "email"
        );
    }

    private String getOAuthServiceName(OAuth2UserRequest userRequest) {
        return userRequest.getClientRegistration()
                .getRegistrationId();
    }
}
