package umc.liview.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.auth.application.dto.TokenReissueCommand;
import umc.liview.auth.domain.UserRepository;
import umc.liview.auth.presentation.dto.response.TokenResponse;
import umc.liview.config.jwt.factory.JwtFactory;
import umc.liview.config.jwt.utils.JwtExtractor;
import umc.liview.config.jwt.utils.JwtVerifier;
import umc.liview.exception.NotFoundException;
import umc.liview.exception.code.ErrorCode;
import umc.liview.user.domain.User;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtFactory jwtFactory;
    private final JwtExtractor jwtExtractor;
    private final JwtVerifier jwtVerifier;
    private final UserRepository userRepository;

    @Transactional
    public TokenResponse reissue(TokenReissueCommand tokenReissueCommand) {
        // 토큰 -> user's id
        Long userId = jwtExtractor.getUserId(tokenReissueCommand.accessToken());
        // 리프레쉬 토큰 검증
        jwtVerifier.verifyRefreshToken(userId, tokenReissueCommand.refreshToken());
        // 토큰 재발급
        return toTokenResponse(userId);
    }

    @Transactional
    public void logout(long userId) {
        User user = findUser(userId);
        jwtVerifier.expireRefreshToken(user.getId());
    }

    private TokenResponse toTokenResponse(long userId) {
        return TokenResponse.builder()
                .accessToken(jwtFactory.createAccessToken(userId))
                .refreshToken(jwtFactory.createRefreshToken(userId))
                .build();
    }

    private User findUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND, userId));
    }

    private User findUserWithEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND, "일치하는 이메일의 유저가 없습니다."));
    }
}
