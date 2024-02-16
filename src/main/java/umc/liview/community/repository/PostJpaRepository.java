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

    // 포스팅된 게시글 조회 - 기간별
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
    public List<PostInfo> findPostsSortedByDate(int page) {
        return em.createQuery(
                "select new umc.liview.community.service.dto.response.PostInfo(t.post.id, ti.imageUrl)" +
                            " from Tour t" +
                            " join t.tourImages ti" +
                            " left join t.post p" +
                            " where ti.isThumbnail = true and p.postStatus = :postStatus" +
                            " order by p.createdAt desc ", PostInfo.class)
                .setParameter("postStatus", Post.PostStatus.PUBLIC)
                .setFirstResult(page)
                .setMaxResults(20)
                .getResultList();
    }

    // 게시글 조회
    public List<PostInfo> findPostsSortedByViews(int page) {
        return em.createQuery(
                        "select new umc.liview.community.service.dto.response.PostInfo(t.post.id, ti.imageUrl)" +
                                " from Tour t" +
                                " join t.tourImages ti" +
                                " left join t.post p" +
                                " where ti.isThumbnail = true and p.postStatus = :postStatus" +
                                " order by p.viewCounts desc ", PostInfo.class)
                .setParameter("postStatus", Post.PostStatus.PUBLIC)
                .setFirstResult(page)
                .setMaxResults(20)
                .getResultList();
    }
}
