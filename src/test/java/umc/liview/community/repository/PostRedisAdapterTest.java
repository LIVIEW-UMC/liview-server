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

        List<String> topLogs = postRedisAdapter.getTopSearchedLogs(3);

        Assertions.assertEquals("log1", topLogs.get(0));
        Assertions.assertEquals("log2", topLogs.get(1));
        Assertions.assertEquals("log3", topLogs.get(2));
    }

}
