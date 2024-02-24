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

        postRedisAdapter.addSearchedLog(userId, firstTourId, 100d);
        postRedisAdapter.addSearchedLog(userId, secondTourId, 110d);
        postRedisAdapter.addSearchedLog(userId, thirdTourId, 120d);
        postRedisAdapter.addSearchedLog(userId, fourthTourId, 130d);
        postRedisAdapter.addSearchedLog(userId, fifthTourId, 140d);

        postRedisAdapter.addSearchedLog(userId, secondTourId, 160d);
        postRedisAdapter.addSearchedLog(userId, firstTourId, 200d);

        List<Long> topLogs = postRedisAdapter.getTopSearchedLogs(userId, 3);

        Assertions.assertEquals(firstTourId, topLogs.get(0));
        Assertions.assertEquals(secondTourId, topLogs.get(1));
        Assertions.assertEquals(fifthTourId, topLogs.get(2));

        Assertions.assertEquals(200d, postRedisAdapter.getScore(userId, firstTourId));
        Assertions.assertEquals(160d, postRedisAdapter.getScore(userId, secondTourId));
        Assertions.assertEquals(150d, postRedisAdapter.getScore(userId, fifthTourId));
    }

}
