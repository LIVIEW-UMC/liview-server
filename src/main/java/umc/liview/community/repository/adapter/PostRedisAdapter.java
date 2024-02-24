package umc.liview.community.repository.adapter;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostRedisAdapter {

    private final String SEARCHED_RECORD_PREFIX_KEY = "ViewedTourIdsByUserId:";
    private final int SEARCHED_RECORD_MAX_SIZE = 10;
    private final RedisTemplate<String, Long> cachingRedisTemplate;

    public PostRedisAdapter(RedisTemplate<String, Long> cachingRedisTemplate) {
        this.cachingRedisTemplate = cachingRedisTemplate;
    }

    // 원소 추가
    public Boolean addSearchedLog(Long userId, Long tourId, Double amount) {
        String key = serializeKey(userId);
        return cachingRedisTemplate.opsForZSet().add(key, tourId, amount);
    }

    // Top 조회 게시글 조회
    public List<Long> getTopSearchedLogs(Long userId, Integer fetchCount) {
        String key = serializeKey(userId);
        return cachingRedisTemplate
                .opsForZSet()
                .reverseRangeByScore(key, 0D, Double.MAX_VALUE, 0, fetchCount)
                .stream().collect(Collectors.toList());
    }

    // score 조회
    // 참여자가 입찰한 금액 조회
    public Double getScore(Long tourId, Long userId) {
        String key = serializeKey(userId);

        // score() 로 score 값 조회
        // 따라서 score 에 저장한 입찰 금액 리턴
        return cachingRedisTemplate.opsForZSet().score(key, tourId);
    }

    // key 설정
    private String serializeKey(Long userId) {
        return SEARCHED_RECORD_PREFIX_KEY + userId;
    }
}
