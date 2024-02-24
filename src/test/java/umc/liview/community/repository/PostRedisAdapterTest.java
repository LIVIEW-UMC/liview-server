package umc.liview.community.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import umc.liview.community.repository.adapter.PostRedisAdapter;

import java.util.List;

@SpringBootTest
class PostRedisAdapterTest {
    private final Long firstTourId = 1L;
    private final Long secondTourId = 2L;
    private final Long thirdTourId = 3L;
    private final Long fourthTourId = 4L;
    private final Long fifthTourId = 5L;
    private final Long userId = 1000L;

    @Autowired
    private PostRedisAdapter postRedisAdapter;

    @Test
    @Profile("local")
    public void simulate() {

        postRedisAdapter.addSearchedLog(userId, "log1");
        postRedisAdapter.addSearchedLog(userId, "log2");
        postRedisAdapter.addSearchedLog(userId, "log3");
        postRedisAdapter.addSearchedLog(userId, "log4");
        postRedisAdapter.addSearchedLog(userId, "log5");

        List<Long> topLogs = postRedisAdapter.getTopSearchedLogs(userId, 3);

        Assertions.assertEquals(firstTourId, topLogs.get(0));
        Assertions.assertEquals(secondTourId, topLogs.get(1));
        Assertions.assertEquals(fifthTourId, topLogs.get(2));

        Assertions.assertEquals(200d, postRedisAdapter.getScore(userId, firstTourId));
        Assertions.assertEquals(160d, postRedisAdapter.getScore(userId, secondTourId));
        Assertions.assertEquals(150d, postRedisAdapter.getScore(userId, fifthTourId));
    }

}
