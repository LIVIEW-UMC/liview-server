package umc.liview;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class LiviewApplicationTests {

	@Test
	void contextLoads() {
		LocalDateTime l  = LocalDateTime.now();
		System.out.println(l);
	}

}
