package umc.liview.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.community.domain.CommentReply;
import umc.liview.community.domain.Comments;
import umc.liview.community.domain.Post;
import umc.liview.community.dto.PostDTO;
import umc.liview.community.repository.*;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.repository.TourRepository;
import umc.liview.tour.service.TourImageService;
import umc.liview.user.domain.User;
import umc.liview.user.domain.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final TourImageService tourImageService;
    private final CommentsRepository commentsRepository;
    private final CommentsLikesRepository commentsLikesRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final CommentReplyLikesRepository commentReplyLikesRepository;

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


    public void togglePostService (Long postId){
        Post post = postRepository.getReferenceById(postId);
        post.toggleActivationStatus();
        postRepository.save(post);
    }


    public List<Tour> getMyNonClassPost(Long userId) {

        return tourRepository.findAllByUserIdAndCompleteStatusAndIsClassified(userId,Tour.CompleteStatus.COMPLETE,false);

    }


    public List<Tour> getOtherNonClassPost(Long userId) {
        List<Tour> tempTourList = tourRepository.findAllByUserIdAndCompleteStatusAndIsClassified(userId,Tour.CompleteStatus.COMPLETE,false);
        List<Tour> tourList = new ArrayList<>();
        for(Tour tour : tempTourList){
            Post post = tour.getPost();
            if (post.getPostStatus() == Post.PostStatus.PUBLIC){
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

        for(Tour tour : tourList){
            PostDTO postDTO = PostDTO.toPostDTO(tour, tourImageService.getThumbnail(tour), tour.getPost().getViewCounts());
            postDTOList.add(postDTO);
        }

        return postDTOList;

    }

    /**
     * 댓글 + 답글 개수 세주는 메서드
     * @param post 포스트
     * @return 댓글, 답글 개수(int)
     */
    public int getCommentCount(Post post){
        List<Comments> commentsList = commentsRepository.findAllByPost(post);
        int commentCount = commentsList.size();
        commentCount += commentsList.stream().mapToInt(comments
                -> commentReplyRepository.countAllByComments(comments).intValue()).sum();
        return commentCount;
    }

    /**
     * 댓글의 좋아요 개수
     * @param comments 댓글 객체
     * @return 댓글 좋아요 개수(Long)
     */
    public Long getCommentsLikeCount(Comments comments){
        return commentsLikesRepository.countByComments(comments);
    }

    /**
     * 답글 좋아요 개수
     * @param commentReply 답글 객체
     * @return 답글 좋아요 개수(Long)
     */
    public Long getCommentReplyLikeCount(CommentReply commentReply){
        return commentReplyLikesRepository.countByCommentReply(commentReply);
    }

    /**
     * 댓글 좋아요 체크
     * @param userId 유저 아이디
     * @param comments 댓글 객체
     * @return 좋아요 했으면 1, 안했으면 0
     */
    public Long checkCommentsLike(Long userId, Comments comments){
        return commentsLikesRepository.countByCommentsAndUserId(comments, userId);
    }

    /**
     * 답글 좋아요 체크
     * @param userId 유저 아이디
     * @param commentReply 답글 객체
     * @return 좋아요 했으면 1, 안했으면 0
     */
    public Long checkCommentReplyLike(Long userId, CommentReply commentReply){
        return commentReplyLikesRepository.countByCommentReplyAndUserId(userId, commentReply);
    }
}
