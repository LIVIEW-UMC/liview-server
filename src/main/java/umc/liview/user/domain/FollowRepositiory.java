package umc.liview.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepositiory extends JpaRepository<Follow,Long> {

    Optional<Follow> findByFollowerIdAndUserId(Long followerId, Long userId);




}
