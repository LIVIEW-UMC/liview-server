package umc.liview.auth.application.dto;

public record TokenReissueCommand(String accessToken, String refreshToken) {
}
