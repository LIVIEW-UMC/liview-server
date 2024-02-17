package umc.liview.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.community.domain.CommentReply;

public interface CommentReplyRepository extends JpaRepository<CommentReply, Long> {
}
