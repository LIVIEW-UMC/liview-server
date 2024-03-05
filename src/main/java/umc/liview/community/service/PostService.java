package umc.liview.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.community.domain.Likes;
import umc.liview.community.domain.Post;
import umc.liview.community.dto.PostDTO;
import umc.liview.community.repository.LikesRepository;
import umc.liview.community.repository.adapter.PostJpaAdapter;
import umc.liview.community.repository.adapter.PostRedisAdapter;
import umc.liview.community.repository.PostRepository;
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
import umc.liview.user.domain.User;
import umc.liview.user.domain.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final PostJpaAdapter postJpaAdapter;
    private final TourImageService tourImageService;
    private final PostRedisAdapter postRedisAdapter;
    private final TagService tagService;
    private final LikesRepository likesRepository;

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
            PostDTO postDTO = PostDTO.toPostDTO(tour, tourImageService.getThumbnail(tour), tour.getPost());
            postDTOList.add(postDTO);
        }

        return postDTOList;
    }

    // 게시글 조회 - 시간순, 조회수
    @Transactional(readOnly = true)
    public List<PostInfo> findPostInfos(Long userId, String sortedBy, int page) {
        verifyUser(userId);
        return findPostInfos(sortedBy, page);
    }

    // 게시글 검색 - 시간순, 조회수
    @Transactional
    public List<PostInfo> searchPostInfos(Long userId, String searchValue, String sortedBy, int page) {
        // 검색어 저장
        verifyUser(userId);
        saveUserSearchedWord(userId, searchValue);
        saveSearchedWordToRanks(searchValue);

        // 검색
        List<Long> searchedTours = searchTourIds(searchValue, page);
        return searchPostInfos(searchedTours, sortedBy);
    }

    // 검색어 랭킹 조회
    @Transactional(readOnly = true)
    public List<String> findSearchLogs() {
        return findMostSearchedWord();
    }

    // 조회한 게시글 조회
    @Transactional(readOnly = true)
    public List<PostInfo> findViewedTours(Long userId) {
        List<Long> viewedTourIds = findViewedTourIds(userId);
        return searchPostInfos(viewedTourIds, "date");
    }

    // 조회한 게시글 기록 삭제
    @Transactional
    public void deleteViewedTourLogs(Long userId, List<Long> tourIds) {
        postRedisAdapter.deleteViewedToursId(userId, tourIds);
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
    public DetailIncompletedTourDTO getDetailPostService(Long userId, Long tourId) {

        Tour tour = getTour(tourId);
        Post post = tour.getPost();
        List<TourImages> tourImagesList = new ArrayList<>();
        tourImagesList.add(tourImageService.getThumbnailDetail(tourId));
        tourImagesList.addAll(tourImageService.getNotThumbailDetail(tourId));
        increaseViewCount(post);
        postRedisAdapter.saveViewedTourIds(userId, tourId);

        return DetailIncompletedTourDTO.builder()
                .tourId(tourId)
                .contents(tour.getContents())
                .title(tour.getTitle())
                .hashtag(tagService.getHashtag(tourId))
                .imgList(tourImagesList)
                .build();
    }


    @Transactional
    public void likePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("post not found"));
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("user not found"));

        Optional<Likes> existingLikes = likesRepository.findByPostAndUserId(post,userId);
        if(existingLikes.isPresent()) {
            likesRepository.delete(existingLikes.get());

        }else {
            likesRepository.save(new Likes(userId, post));
        }
    }

    @Transactional
    public String likeStatusService(Long postId,Long userId) {
        Post post = postRepository.findById(postId).orElseThrow(()-> new RuntimeException("post not found"));
        Optional<Likes> existingLikes = likesRepository.findByPostAndUserId(post,userId);
        if(existingLikes.isPresent()) {
            return "true";
        }else {
            return "false";
        }

    }

    @Transactional
    public Long getUserId(Long tourId) {
        Tour tour = tourRepository.getReferenceById(tourId);
        return tour.getUser().getId();
    }

    private List<Long> findViewedTourIds(Long userId) {
        return postRedisAdapter.getViewedTourIds(userId);
    }

    private List<String> findMostSearchedWord() {
        return postRedisAdapter.getTopSearchedLogs(10);
    }

    private void saveSearchedWordToRanks(String searchValue) {
        postRedisAdapter.addLogsToTopRank(searchValue);
    }

    private void saveUserSearchedWord(Long userId, String searchValue) {
        postRedisAdapter.addSearchedLog(userId, searchValue);
    }

    private void verifyUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND, userId));
    }

    private List<Long> searchTourIds(String searchValue, int page) {
        return postJpaAdapter.searchTours(page, searchValue);
    }

    private List<PostInfo> searchPostInfos(List<Long> searchedTourIds, String sortedBy) {
        return switch (sortedBy) {
            case "date" -> postJpaAdapter.searchPostsByDate(searchedTourIds);
            case "views" -> postJpaAdapter.searchPostsByViews(searchedTourIds);
            default -> throw new BusinessException(ErrorCode.INVALID_REQUEST_PARAMETER);
        };
    }

    private List<PostInfo> findPostInfos(String sortedBy, int page) {
        return switch (sortedBy) {
            case "date" -> postJpaAdapter.findPostsSortedByDate(page);
            case "views" -> postJpaAdapter.findPostsSortedByViews(page);
            default -> throw new BusinessException(ErrorCode.INVALID_REQUEST_PARAMETER);
        };
    }
}
