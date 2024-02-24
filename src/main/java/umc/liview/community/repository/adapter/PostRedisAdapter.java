package umc.liview.community.repository.adapter;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import umc.liview.common.utils.formatter.TimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PostRedisAdapter {

    private final String SEARCHED_WORD_PREFIX_KEY = "SearchedLogsByUserId:";
    private final String SEARCH_WORD_RANKING = "SearchedLogRanks";
    private final int SEARCHED_RECORD_MAX_SIZE = 10;
    private final RedisTemplate<String, String> redisTemplate;

    public PostRedisAdapter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 검색어 저장
    public void addSearchedLog(Long userId, String log) {
        String key = serializeKey(userId);
        redisTemplate.opsForZSet().add(key, log, TimeFormatter.calculateCurrentTimes());
        redisTemplate.opsForZSet().removeRange(
                key,
                -(SEARCHED_RECORD_MAX_SIZE +1),
                -(SEARCHED_RECORD_MAX_SIZE +1)
        );
    }

    // 검색어 조회
    public List<String> findSearchedLogs(Long userId) {
        String key = serializeKey(userId);
        return new ArrayList<>(Objects.requireNonNull(
                redisTemplate.opsForZSet().reverseRange(key, 0, -1)
        ));
    }

    // 인기 검색어 저장
    public void addLogsToTopRank(String log) {
        redisTemplate.opsForZSet().incrementScore(SEARCH_WORD_RANKING, log, 1D);
    }

    // 인기 검색어 조회
    public List<String> getTopSearchedLogs(Integer fetchCount) {
        return redisTemplate
                .opsForZSet()
                .reverseRangeByScore(SEARCH_WORD_RANKING, 0D, Double.MAX_VALUE, 0, fetchCount)
                .stream().collect(Collectors.toList());
    }

    // key 설정
    private String serializeKey(Long userId) {
        return SEARCHED_WORD_PREFIX_KEY + userId;
    }
}
