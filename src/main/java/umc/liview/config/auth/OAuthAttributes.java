package umc.liview.config.auth;

import lombok.AccessLevel;
import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder(access = AccessLevel.PRIVATE)
public record OAuthAttributes(String provider, Map<String, Object> attributes, String nameAttributesKey, String name, String email, String profileImageUrl) {

    public static OAuthAttributes of(String socialName, Map<String, Object> attributes) {
        if ("kakao".equals(socialName)) {
            return ofKakao(attributes);
        } else if ("google".equals(socialName)) {
            return ofGoogle(attributes);
        } else if ("naver".equals(socialName)) {
            return ofNaver("id", attributes);
        }

        return null;
    }

    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .provider("google")
                .name(String.valueOf(attributes.get("name")))
                .email(String.valueOf(attributes.get("email")))
                .profileImageUrl(String.valueOf(attributes.get("picture")))
                .attributes(attributes)
                .nameAttributesKey("sub")
                .build();
    }

    private static OAuthAttributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                .provider("kakao")
                .name(String.valueOf(kakaoProfile.get("nickname")))
                .email(String.valueOf(kakaoAccount.get("email")))
                .profileImageUrl(String.valueOf(kakaoProfile.get("profile_image_url")))
                .nameAttributesKey("id")
                .attributes(attributes)
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return OAuthAttributes.builder()
                .provider("naver")
                .name(String.valueOf(response.get("nickname")))
                .email(String.valueOf(response.get("email")))
                .profileImageUrl(String.valueOf(response.get("profile_image")))
                .attributes(response)
                .nameAttributesKey(userNameAttributeName)
                .build();
    }

    public Map<String, Object> convertAttributesToMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("provider", provider);
        map.put("name", name);
        map.put("email", email);
        map.put("imageUrl", profileImageUrl);
        return map;
    }
}
