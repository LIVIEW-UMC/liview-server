package umc.liview.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.community.domain.Comments;
import umc.liview.community.domain.Post;
import umc.liview.user.domain.User;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments,Long> {
    List<Comments> findAllByPost_Id(Long postId);
    Long countAllByPost_IdAndUser_Id(Long postId, Long userId);
}
