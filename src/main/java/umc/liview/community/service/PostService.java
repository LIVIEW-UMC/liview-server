package umc.liview.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.community.domain.Post;
import umc.liview.community.dto.PostDTO;
import umc.liview.community.repository.PostJpaRepository;
import umc.liview.community.repository.PostRedisRepository;
import umc.liview.community.repository.PostRepository;
import umc.liview.community.service.dto.request.SearchLog;
import umc.liview.community.service.dto.response.PostInfo;
import umc.liview.exception.BusinessException;
import umc.liview.exception.NotFoundException;
import umc.liview.exception.code.ErrorCode;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.domain.TourImages;
import umc.liview.tour.dto.DetailIncompletedTourDTO;
import umc.liview.tour.dto.SimpleTourDTO;
import umc.liview.tour.repository.TourRepository;
import umc.liview.tour.service.TagService;
import umc.liview.tour.service.TourImageService;
import umc.liview.tour.service.TourService;
import umc.liview.user.domain.User;
import umc.liview.user.domain.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final PostJpaRepository postJpaRepository;
    private final TourImageService tourImageService;
    private final PostRedisRepository postRedisRepository;
    private final TagService tagService;

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

    @Transactional
    public List<PostDTO> getMyAllPostService(Long myId) {
        List<PostDTO> postDTOList = new ArrayList<>();

        List<Tour> tourList = tourRepository.findAllByUserIdAndCompleteStatus(myId, Tour.CompleteStatus.COMPLETE);

        for (Tour tour : tourList) {
            PostDTO postDTO = PostDTO.toPostDTO(tour, tourImageService.getThumbnail(tour), tour.getPost().getViewCounts());
            postDTOList.add(postDTO);
        }

        return postDTOList;

    }

    // 게시글 조회 - 시간순, 조회수
    @Transactional
    public List<PostInfo> findPostInfos(Long userId, String sortedBy, int page) {
        verifyAndFindUser(userId);
        return findPosts(sortedBy, page);
    }

    // 게시글 검색 - 시간순, 조회수
    @Transactional
    public List<PostInfo> searchPostInfos(Long userId, String searchValue, String sortedBy, int page) {
        // 검색기록 저장
        User user = verifyAndFindUser(userId);
        postRedisRepository.saveSearchLog(user.getId(), new SearchLog(searchValue));
        // 검색
        List<Long> searchedTours = searchTours(searchValue, page);
        return searchPosts(searchedTours, sortedBy);
    }

    // 검색기록 조회
    public List<SearchLog> findSearchLogs(Long userId) {
        return postRedisRepository.findRecentSearchLogs(userId);
    }

    private User verifyAndFindUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND, userId));
    }

    private List<Long> searchTours(String searchValue, int page) {
        return postJpaRepository.searchTours(page, searchValue);
    }

    private List<PostInfo> searchPosts(List<Long> searchedTourIds, String sortedBy) {
        return switch (sortedBy) {
            case "date" -> postJpaRepository.searchPostsByDate(searchedTourIds);
            case "views" -> postJpaRepository.searchPostsByViews(searchedTourIds);
            default -> throw new BusinessException(ErrorCode.INVALID_REQUEST_PARAMETER);
        };
    }

    private List<PostInfo> findPosts(String sortedBy, int page) {
        return switch (sortedBy) {
            case "date" -> postJpaRepository.findPostsSortedByDate(page);
            case "views" -> postJpaRepository.findPostsSortedByViews(page);
            default -> throw new BusinessException(ErrorCode.INVALID_REQUEST_PARAMETER);
        };
    }

    @Transactional
    public List<SimpleTourDTO> getNonClassPostService(Long myId, Long userId) {
        //내 거 조회 -> 전체
        if (myId.equals(userId)) {
            List<Tour> tourList = getMyNonClassPost(myId);
            return putImage(tourList);

        }
        //상대거 조회 공개만 조회
        else{
            List<Tour> tourList = getOtherNonClassPost(userId);
            return putImage(tourList);

        }

    }


    @Transactional
    public Tour getTour(Long tourId) {
        return tourRepository.getReferenceById(tourId);
    }



    @Transactional
    public List<SimpleTourDTO> putImage(List<Tour> tourList){
        List<SimpleTourDTO> simpleTourDTOList = new ArrayList<>();
        if (!tourList.isEmpty()) {
            for(Tour tour : tourList){
                simpleTourDTOList.add(
                        SimpleTourDTO.builder()
                                .tourId(tour.getId())
                                .title(tour.getTitle())
                                .localDateTime(tour.getCreatedAt())
                                .imageURL(tourImageService.getThumbnail(tour))
                                .size(tour.getSize())
                                .build());
            }
        }
        return simpleTourDTOList;
    }

    @Transactional
    public DetailIncompletedTourDTO getDetailPostService(Long tourId) {

        Tour tour = getTour(tourId);
        Post post = tour.getPost();
        List<TourImages> tourImagesList = new ArrayList<>();
        tourImagesList.add(tourImageService.getThumbnailDetail(tourId));
        tourImagesList.addAll(tourImageService.getNotThumbailDetail(tourId));
        increaseViewCount(post);

        return DetailIncompletedTourDTO.builder()
                .tourId(tourId)
                .contents(tour.getContents())
                .title(tour.getTitle())
                .hashtag(tagService.getHashtag(tourId))
                .imgList(tourImagesList)
                .build();
    }


}
