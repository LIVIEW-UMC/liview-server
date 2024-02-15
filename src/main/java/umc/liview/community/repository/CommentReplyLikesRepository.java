package umc.liview.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.community.domain.CommentReply;
import umc.liview.community.domain.CommentReplyLikes;
import umc.liview.community.domain.Comments;

import java.util.List;

public interface CommentReplyLikesRepository extends JpaRepository<CommentReplyLikes, Long> {

    List<CommentReplyLikes> findAllByCommentReplyAndUserId(CommentReply commentReply, Long userId);
}
