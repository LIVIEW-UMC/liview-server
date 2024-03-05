package umc.liview.community.repository.adapter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import umc.liview.common.utils.formatter.TimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PostRedisAdapter {

    private final String SEARCHED_WORD_PREFIX_KEY = "SearchedLogsByUserId:";
    private final String SEARCHED_WORD_RANKING = "SearchedLogRanks";
    private final int SEARCHED_RECORD_MAX_SIZE = 10;
    private final String VIEWED_TOUR_PREFIX_KEY = "ViewedToursByUserId:";

    private final RedisTemplate<String, String> redisTemplate;

    public PostRedisAdapter(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 검색어 저장
    public void addSearchedLog(Long userId, String log) {
        String key = serializeSearchedLogsKey(userId);
        redisTemplate.opsForZSet().add(key, log, TimeFormatter.calculateCurrentTimes());
        redisTemplate.opsForZSet()
                .removeRange(
                        key,
                        -(SEARCHED_RECORD_MAX_SIZE +1),
                        -(SEARCHED_RECORD_MAX_SIZE +1)
        );
    }

    // 검색어 조회
    public List<String> findSearchedLogs(Long userId) {
        String key = serializeSearchedLogsKey(userId);
        return new ArrayList<>(Objects.requireNonNull(
                redisTemplate.opsForZSet()
                        .reverseRange(key, 0, -1)
        ));
    }

    // 검색어 삭제
    public void deleteSearchedLogs(Long userId, String log) {
        String key = serializeSearchedLogsKey(userId);
        redisTemplate.opsForZSet().remove(key, log);
    }

    // 인기 검색어 저장
    public void addLogsToTopRank(String log) {
        redisTemplate.opsForZSet().incrementScore(SEARCHED_WORD_RANKING, log, 1D);
    }

    // 인기 검색어 조회
    public List<String> getTopSearchedLogs(Integer fetchCount) {
        return new ArrayList<>(Objects.requireNonNull(
                redisTemplate.opsForZSet()
                        .reverseRangeByScore(SEARCHED_WORD_RANKING, 0D, Double.MAX_VALUE, 0, fetchCount)));
    }

    // 조회한 게시글 기록 저장
    public void saveViewedTourIds(Long userId, Long tourId) {
        String key = serializeViewedToursKey(userId);
        redisTemplate.opsForSet().add(key, String.valueOf(tourId));
    }

    // 조회한 게시글 기록 조회
    public List<Long> getViewedTourIds(Long userId) {
        String key = serializeViewedToursKey(userId);
        Set<String> viewedTourIds = redisTemplate.opsForSet().members(key);
        return viewedTourIds.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    // 조회한 기록 삭제
    public void deleteViewedToursId(Long userId, Long tourId) {
        String key = serializeViewedToursKey(userId);
        redisTemplate.opsForSet().remove(key, String.valueOf(tourId));
    }

    // TODO: 1주일 간격으로 조회한 게시글 기록 삭제 로직
    // 삭제하는 로직 설계 -> 스케쥴러로 1주일에 싹다 삭제
    public void deleteUsersLogs() {
        String searchedLogKeyPattern = "^" + SEARCHED_WORD_PREFIX_KEY + ".*";
        String viewedToursIdKeyPatter = "^" + VIEWED_TOUR_PREFIX_KEY + ".*";

//        Set<String>
    }

    // key 설정
    private String serializeSearchedLogsKey(Long userId) {
        return SEARCHED_WORD_PREFIX_KEY + userId;
    }
    private String serializeViewedToursKey(Long userId) {
        return VIEWED_TOUR_PREFIX_KEY + userId;
    }
}
