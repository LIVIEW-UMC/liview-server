package umc.liview.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.community.domain.Comments;
import umc.liview.community.domain.Post;

import java.util.List;

public interface CommentsRepository extends JpaRepository<Comments,Long> {
    List<Comments> findAllByPost(Post post);
}
