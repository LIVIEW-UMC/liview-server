package umc.liview.community.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
;
import umc.liview.community.domain.Post;
import umc.liview.community.service.dto.response.PostInfo;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostJpaRepository {

    private final EntityManager em;

    // 포스팅된 게시글 조회 - 날짜순
    public List<Long> findPostsSortedByDate(int page) {
        return em.createQuery(
                "select p.id" +
                                " from Post p" +
                                " where p.postStatus = :postStatus" +
                                " order by p.createdAt desc", Long.class
                )
                .setParameter("postStatus", Post.PostStatus.PUBLIC)
                .setFirstResult(page)
                .setMaxResults(20)
                .getResultList();
    }

    // 포스팅된 게시글 조회 - 조회순
    public List<Long> findPostsSortedByViews(int page) {
        return em.createQuery(
                        "select p.id" +
                                " from Post p" +
                                " where p.postStatus = :postStatus" +
                                " order by p.viewCounts desc", Long.class
                )
                .setParameter("postStatus", Post.PostStatus.PUBLIC)
                .setFirstResult(page)
                .setMaxResults(20)
                .getResultList();
    }

    // 포스팅된 게시글 조회 - 조회순
    public List<Long> findPostsInDuration(String startDay, String endDay, int page) {
        return em.createQuery(
                "select p.id" +
                                " from Post p" +
                                " join p.tour t" +
                                " where p.postStatus = Post.PostStatus.PUBLIC" +
                                " and t.startDay > :startDay" +
                                " and t.endDay < :endDay", Long.class
                )
                .setParameter("startDay", startDay)
                .setParameter("endDay", endDay)
                .setFirstResult(page)
                .setMaxResults(20)
                .getResultList();
    }

    // 게시글 조회
    public List<PostInfo> findPublishedPosts(List<Long> postIds) {
        return em.createQuery(
                "select new umc.liview.community.service.dto.response.PostInfo(t.post.id, ti.imageUrl)" +
                                " from TourImages ti" +
                                " join ti.tour t" +
                                " where t.post.id in :postIds and ti.isThumbnail = true", PostInfo.class)
                .setParameter("postIds", postIds)
                .getResultList();
    }
}
