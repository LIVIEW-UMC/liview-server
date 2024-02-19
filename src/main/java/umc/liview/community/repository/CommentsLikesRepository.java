package umc.liview.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.community.domain.Comments;
import umc.liview.community.domain.CommentsLikes;

import java.util.List;

public interface CommentsLikesRepository extends JpaRepository<CommentsLikes, Long> {
    List<CommentsLikes> findAllByCommentsAndAndUserId(Comments comments, Long userId);
    Long countAllByComments(Comments comments);
}
