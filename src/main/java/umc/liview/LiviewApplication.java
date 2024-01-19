package umc.liview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LiviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(LiviewApplication.class, args);
	}

}
