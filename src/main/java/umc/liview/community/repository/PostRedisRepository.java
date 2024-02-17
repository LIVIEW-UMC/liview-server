package umc.liview.community.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import umc.liview.community.service.dto.request.SearchLog;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRedisRepository {

    private final RedisTemplate<String, SearchLog> redisTemplate;

    // 검색기록 저장
    public void saveSearchLog(Long userId, SearchLog searchLog) {
        String key = "SearchedLog" + userId;
        verifyLogsSizeExceed(key);
        redisTemplate.opsForList().leftPush(key, searchLog);
    }

    // 검색기록 조회
    public List<SearchLog> findRecentSearchLogs(Long userId) {
        String key = "SearchLog" + userId;
        return redisTemplate.opsForList().range(key, 0, 10);
    }

    // 검색기록 삭제
    public void deleteRecentSearchLog(Long userId, SearchLog searchLog) {
        String key = "SearchLog" + userId;
        redisTemplate.opsForList().remove(key, 1, searchLog);
    }

    private void verifyLogsSizeExceed(String key) {
        Long size = redisTemplate.opsForList().size(key);
        if (size == 10) {
            redisTemplate.opsForList().rightPop(key);
        }
    }
}
