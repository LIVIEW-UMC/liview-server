package umc.liview.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.community.domain.Likes;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
