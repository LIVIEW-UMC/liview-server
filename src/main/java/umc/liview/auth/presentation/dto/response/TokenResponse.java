package umc.liview.auth.presentation.dto.response;

import lombok.Builder;

@Builder
public record TokenResponse(String accessToken, String refreshToken) {
}
