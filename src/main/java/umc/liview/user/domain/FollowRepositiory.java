package umc.liview.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepositiory extends JpaRepository<Follow,Long> {

    Optional<Follow> findByFollowerIdAndUserId(Long followerId, Long userId);
    Optional<Long> countFollowByFollowerId(Long followerId);
    Optional<Long> countFollowByUserId(Long UserId);
}
