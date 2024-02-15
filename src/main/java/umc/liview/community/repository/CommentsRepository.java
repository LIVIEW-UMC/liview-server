package umc.liview.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.community.domain.Comments;

public interface CommentsRepository extends JpaRepository<Comments,Long> {
}
