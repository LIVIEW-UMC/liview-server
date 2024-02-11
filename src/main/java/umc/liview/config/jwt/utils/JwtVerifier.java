package umc.liview.config.jwt.utils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import umc.liview.config.jwt.JwtException;
import umc.liview.exception.NotFoundException;
import umc.liview.exception.code.ErrorCode;

import java.security.Key;
import java.util.Optional;

import static umc.liview.config.jwt.JwtException.*;

@Component
public class JwtVerifier {

    private final Key key;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtVerifier(
            RedisTemplate<String, String> redisTemplate,
            @Value("${jwt.secret}") String secretKey
    ) {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
        this.redisTemplate = redisTemplate;
    }

    public boolean validateToken(String token, HttpServletRequest request) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException | SignatureException e) {
            setAttribute(request, WRONG_TOKEN);
        } catch (ExpiredJwtException e) {
            setAttribute(request, EXPIRED_TOKEN);
        }
        return false;
    }

    public void verifyRefreshToken(long userId, String refreshToken) {
        String storedRefreshToken = findRefreshToken(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
        if(!storedRefreshToken.equals(refreshToken))
            throw new NotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }

    public void expireRefreshToken(long userId) {
        redisTemplate.delete(String.valueOf(userId));
    }

    private Optional<String> findRefreshToken(long userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(String.valueOf(userId)));
    }


    private void setAttribute(HttpServletRequest request, JwtException exception) {
        request.setAttribute("exception", exception.name());
    }
}
