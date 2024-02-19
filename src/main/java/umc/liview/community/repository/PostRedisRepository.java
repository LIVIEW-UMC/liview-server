package umc.liview.community.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import umc.liview.community.service.dto.request.SearchLogCommand;

import java.util.List;

@Repository
public class PostRedisRepository {

    private final String SEARCHED_RECORD_PREFIX_KEY = "SearchedLog:";
    private final int SEARCHED_RECORD_MAX_SIZE = 10;
    private final RedisTemplate<String, Object> redisTemplate;

    public PostRedisRepository(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 검색기록 저장
    public void saveSearchValue(Long userId, SearchLogCommand searchLogCommand) {
        String key = SEARCHED_RECORD_PREFIX_KEY + userId;
        List<Object> searchedRecordList = redisTemplate.opsForList().range(key, 0, -1);

        if (!searchedRecordList.contains(searchLogCommand.name())) {
            redisTemplate.opsForList().rightPush(key, searchLogCommand.name());
        }
    }

    // 검색기록 조회
    public List<SearchLogCommand> findRecentSearchLogs(Long userId) {
        return null;
    }

    // 검색기록 삭제
    public void deleteRecentSearchLog(Long userId, SearchLogCommand searchLogCommand) {

    }
}
