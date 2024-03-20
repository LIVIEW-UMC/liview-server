package umc.liview.community.repository.adapter;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
;
import umc.liview.community.domain.Post;
import umc.liview.community.service.dto.response.PostInfo;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostJpaAdapter {

    private final EntityManager em;

    // 게시글 조회- 날짜순
    public List<PostInfo> findPostsSortedByDate(int page) {
        return em.createQuery(
                "select new umc.liview.community.service.dto.response.PostInfo(t.post.id, t.id, ti.imageUrl)" +
                            " from Tour t" +
                            " join t.tourImages ti" +
                            " left join t.post p" +
                            " where ti.isThumbnail = true and p.postStatus = :postStatus" +
                            " order by p.createdAt desc", PostInfo.class)
                .setParameter("postStatus", Post.PostStatus.PUBLIC)
                .setFirstResult(page*20)
                .setMaxResults(20)
                .getResultList();
    }

    // 게시글 조회 - 조회순
    public List<PostInfo> findPostsSortedByViews(int page) {
        return em.createQuery(
                "select new umc.liview.community.service.dto.response.PostInfo(t.post.id, t.id, ti.imageUrl)" +
                            " from Tour t" +
                            " join t.tourImages ti" +
                            " left join t.post p" +
                            " where ti.isThumbnail = true and p.postStatus = :postStatus" +
                            " order by p.viewCounts desc", PostInfo.class)
                .setParameter("postStatus", Post.PostStatus.PUBLIC)
                .setFirstResult(page*20)
                .setMaxResults(20)
                .getResultList();
    }

    // 일정 검색
    public List<Long> searchTours(int page, String searchValue) {
        return em.createQuery(
                "select t.id" +
                            " from TourTags tt" +
                            " join tt.tag ta" +
                            " join tt.tour t" +
                            " where t.title like concat('%', :searchValue, '%')" +
                            " or ta.name like concat('%', :searchValue, '%')", Long.class)
                .setParameter("searchValue", searchValue)
                .setFirstResult(page*20)
                .setMaxResults(20)
                .getResultList();
    }

    // 일정 -> 게시글 검색(생성일)
    public List<PostInfo> searchPostsByDate(List<Long> tourIds) {
        return em.createQuery(
                "select new umc.liview.community.service.dto.response.PostInfo(t.post.id, t.id, ti.imageUrl)" +
                            " from Tour t" +
                            " join t.tourImages ti" +
                            " left join t.post p" +
                            " where t.id in :tourIds" +
                            " and ti.isThumbnail = true and p.postStatus = :postStatus" +
                            " order by p.createdAt desc", PostInfo.class)
                .setParameter("tourIds", tourIds)
                .setParameter("postStatus", Post.PostStatus.PUBLIC)
                .getResultList();
    }

    // 일정 -> 게시글 검색(조회수)
    public List<PostInfo> searchPostsByViews(List<Long> tourIds) {
        return em.createQuery(
                "select new umc.liview.community.service.dto.response.PostInfo(t.post.id, t.id, ti.imageUrl)" +
                            " from Tour t" +
                            " join t.tourImages ti" +
                            " left join t.post p" +
                            " where t.id in :tourIds" +
                            " and ti.isThumbnail = true and p.postStatus = :postStatus" +
                            " order by p.viewCounts desc", PostInfo.class)
                .setParameter("tourIds", tourIds)
                .setParameter("postStatus", Post.PostStatus.PUBLIC)
                .getResultList();
    }
}
