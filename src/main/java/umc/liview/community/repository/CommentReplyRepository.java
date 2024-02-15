package umc.liview.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.community.domain.CommentReply;
import umc.liview.community.domain.Comments;

public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {
    Long countAllByComments(Comments comments);
}
