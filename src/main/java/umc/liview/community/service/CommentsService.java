package umc.liview.community.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.community.converter.CommentsConverter;
import umc.liview.community.domain.*;
import umc.liview.community.dto.CommentsRequestDTO;
import umc.liview.community.dto.CommentsResponseDTO;
import umc.liview.community.repository.*;
import umc.liview.exception.NotFoundException;
import umc.liview.exception.code.ErrorCode;
import umc.liview.user.domain.User;
import umc.liview.user.domain.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentsService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentsRepository commentsRepository;
    private final CommentsLikesRepository commentsLikesRepository;
    private final CommentReplyRepository commentReplyRepository;
    private final CommentReplyLikesRepository commentReplyLikesRepository;

    @Transactional
    public void postComments(Long postId, Long userId, CommentsRequestDTO.postComments postComments){
        User user = getUserById(userId);
        Post post = getPostById(postId);
        createComments(user, post, postComments.getContents());
    }

    @Transactional
    public void likeComments(Long commentsId, Long user_id){
        getUserById(user_id);
        Comments comments = getCommentsById(commentsId);
        createOrDeleteCommentsLike(user_id, comments);
    }

    @Transactional
    public void postReply(Long userId, Long commentsId, CommentsRequestDTO.postComments postComments){
        User user = getUserById(userId);
        Comments comments = getCommentsById(commentsId);
        createReply(user, comments, postComments.getContents());
    }

    @Transactional
    public void likeReply(Long replyId, Long user_id){
        getUserById(user_id);
        CommentReply reply = getReplyById(replyId);
        createOrDeleteReplyLike(user_id, reply);
    }

    private User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND, userId));
    }

    private Post getPostById(Long postId){
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND, postId));
    }

    private Comments getCommentsById(Long commentsId){
        return commentsRepository.findById(commentsId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENTS_NOT_FOUND, commentsId));
    }

    private CommentReply getReplyById(Long replyId){
        return commentReplyRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REPLY_NOT_FOUND, replyId));
    }

    private void createComments(User user, Post post, String contents){
        Comments comments = CommentsConverter.makeComments(user, post, contents);
        commentsRepository.save(comments);
        user.getComments().add(comments);
        post.getComments().add(comments);
    }

    private void createReply(User user, Comments comments, String contents){
        CommentReply commentReply = CommentsConverter.makeReply(user, comments, contents);
        commentReplyRepository.save(commentReply);
        user.getCommentReply().add(commentReply);
        comments.getCommentReply().add(commentReply);
    }

    private void createOrDeleteCommentsLike(Long userId, Comments comments){
        List<CommentsLikes> allByCommentsAndAndUserId = commentsLikesRepository.findAllByCommentsAndAndUserId(comments, userId);

        if (allByCommentsAndAndUserId.isEmpty()){
            commentsLikesRepository.save(CommentsLikes.builder()
                    .comments(comments)
                    .userId(userId)
                    .build());
        }
        else{
            commentsLikesRepository.delete(allByCommentsAndAndUserId.get(0));
        }
    }

    private void createOrDeleteReplyLike(Long userId, CommentReply commentReply){
        List<CommentReplyLikes> reply = commentReplyLikesRepository.findAllByCommentReplyAndUserId(commentReply, userId);

        if (reply.isEmpty()){
            commentReplyLikesRepository.save(CommentReplyLikes.builder()
                    .commentReply(commentReply)
                    .userId(userId)
                    .build());
        }
        else{
            commentReplyLikesRepository.delete(reply.get(0));
        }
    }
}
