package umc.liview.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.community.domain.Post;
import umc.liview.community.dto.PostDTO;
import umc.liview.community.repository.PostJpaRepository;
import umc.liview.community.repository.PostRepository;
import umc.liview.community.service.dto.request.PostedDurationQueryDto;
import umc.liview.community.service.dto.response.PostInfo;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.repository.TourRepository;
import umc.liview.tour.service.TourImageService;
import umc.liview.user.domain.User;
import umc.liview.user.domain.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final PostJpaRepository postJpaRepository;
    private final TourImageService tourImageService;

    @Transactional
    public Post createPost(Long userId) {

        //게시물 공개여부 확인
        User user = userRepository.getReferenceById(userId);
        Post.PostStatus postStatus = Post.PostStatus.PRIVATE;

        // 유저 설정 확인
        if (user.getPrivacyStatus().isToursAutoPosted()) {
            postStatus = Post.PostStatus.PUBLIC;
        }

        Post post = Post.builder()
                .viewCounts(0)
                .postStatus(postStatus)
                .build();

        postRepository.save(post);

        return post;
    }

    @Transactional
    public void togglePostService(Long postId) {
        Post post = postRepository.getReferenceById(postId);
        post.toggleActivationStatus();
        postRepository.save(post);
    }

    @Transactional
    public List<Tour> getMyNonClassPost(Long userId) {

        return tourRepository.findAllByUserIdAndCompleteStatusAndIsClassified(userId, Tour.CompleteStatus.COMPLETE, false);

    }

    @Transactional
    public List<Tour> getOtherNonClassPost(Long userId) {
        List<Tour> tempTourList = tourRepository.findAllByUserIdAndCompleteStatusAndIsClassified(userId, Tour.CompleteStatus.COMPLETE, false);
        List<Tour> tourList = new ArrayList<>();
        for (Tour tour : tempTourList) {
            Post post = tour.getPost();
            if (post.getPostStatus() == Post.PostStatus.PUBLIC) {
                tourList.add(tour);
            }
        }

        return tourList;
    }

    @Transactional
    public void increaseViewCount(Post post) {
        post.increaseViewCount();
        postRepository.save(post);
    }

    public List<PostDTO> getMyAllPostService(Long myId) {
        List<PostDTO> postDTOList = new ArrayList<>();

        List<Tour> tourList = tourRepository.findAllByUserIdAndCompleteStatus(myId, Tour.CompleteStatus.COMPLETE);

        for (Tour tour : tourList) {
            PostDTO postDTO = PostDTO.toPostDTO(tour, tourImageService.getThumbnail(tour), tour.getPost().getViewCounts());
            postDTOList.add(postDTO);
        }

        return postDTOList;

    }

    @Transactional(readOnly = true)
    public List<PostInfo> findPostInfos(Long userId, String sortedBy, int page) {
        List<Long> postIds = findPosts(sortedBy, page);
        return postJpaRepository.findPublishedPosts(postIds);
    }

    @Transactional(readOnly = true)
    public List<PostInfo> findPostInfosByDate(Long userId, PostedDurationQueryDto durationQueryDto, int page) {
        // postIds 리스트 조회
//        List<Long> postInfos = postJpaRepository.findPostsInDuration(
//                durationQueryDto.startDay(), durationQueryDto.endDay(), page);
        //
        return  null;
    }

    private List<Long> findPosts(String sortedBy, int page) {
        return switch (sortedBy) {
            case "date" -> postJpaRepository.findPostsSortedByDate(page);
            case "views" -> postJpaRepository.findPostsSortedByViews(page);
            default -> null;
        };
    }
}
