package umc.liview.community.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.liview.community.controller.dto.request.PostSearchDto;
import umc.liview.community.domain.Post;
import umc.liview.community.dto.CommentsRequestDTO;
import umc.liview.community.dto.PostDTO;
import umc.liview.community.service.CommentsService;
import umc.liview.community.service.PostService;
import umc.liview.community.service.dto.response.PostInfo;
import umc.liview.config.auth.JwtUserDetails;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.domain.TourImages;
import umc.liview.tour.dto.DetailIncompletedTourDTO;
import umc.liview.tour.dto.SimpleTourDTO;
import umc.liview.tour.service.TagService;
import umc.liview.tour.service.TourImageService;
import umc.liview.tour.service.TourService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final CommentsService commentsService;

    // 게시글 조회 - 조회순, 최신순
    @GetMapping("/community")
    public List<PostInfo> findPosts(@RequestParam String sortedBy, @RequestParam int page, @AuthenticationPrincipal JwtUserDetails jwtUserDetails) {
        return postService.findPostInfos(extractUserId(jwtUserDetails), sortedBy, page);
    }

    // 게시글 조회 - 날짜별
    @GetMapping("/community/search")
    public List<PostInfo> findPostsByDate(@RequestBody PostSearchDto postSearchDto, @RequestParam int page, @AuthenticationPrincipal JwtUserDetails jwtUserDetails) {
        return postService.searchPostInfos(extractUserId(jwtUserDetails), postSearchDto.searchValue(), postSearchDto.sortedBy(), page);
    }

    // 검색기록 랭킹 조회
    @GetMapping("/community/search/rank")
    public List<String> findSearchedLogRanks() {
        return postService.findSearchLogs();
    }

    // 게시글 조회 목록 조회
    @GetMapping("/community/viewed-posts")
    public List<PostInfo> findViewedTours(@AuthenticationPrincipal JwtUserDetails jwtUserDetails) {
        return postService.findViewedTours(extractUserId(jwtUserDetails));
    }

    // 게시글 조회 기록 삭제
    @DeleteMapping("community/viewed-posts")
    public void deleteViewedPostLogs(@AuthenticationPrincipal JwtUserDetails jwtUserDetails, @RequestParam List<Long> tourIds) {
        postService.deleteViewedTourLogs(extractUserId(jwtUserDetails), tourIds);
    }

    // 게시글 공개, 비공개 수정
    @PatchMapping("/community/post/{postId}")
    public void togglePostController(@PathVariable Long postId){
        postService.togglePostService(postId);
    }

    //미분류 게시물 조회
    @GetMapping("/community/post/{userId}")
    public List<SimpleTourDTO> getNonClassPostController(@PathVariable Long userId, @AuthenticationPrincipal JwtUserDetails jwtUserDetails ){
        return postService.getNonClassPostService(extractUserId(jwtUserDetails), userId);
    }

    // 게시글 상세 조회
    @GetMapping("/community/post")
    public DetailIncompletedTourDTO getDetailPostController(@RequestParam Long tourId,@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        return postService.getDetailPostService(extractUserId(jwtUserDetails), tourId);
    }

    // 나의 게시글 조회
    @GetMapping("/community/mypost")
    public List<PostDTO>getMyAllPostController(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){
        return postService.getMyAllPostService(extractUserId(jwtUserDetails));
    }

    //게시물 좋아요
    @PostMapping("/community/post/{postId}/likes")
    public void likePost(@AuthenticationPrincipal JwtUserDetails jwtUserDetails, @PathVariable Long postId){
        postService.likePost(postId, extractUserId(jwtUserDetails));
    }

    //특정 유저가 게시물 좋아요 했는지 판단
    @GetMapping("/community/post/{postId}/likes")
    public String likeStatusController(@AuthenticationPrincipal JwtUserDetails jwtUserDetails, @PathVariable Long postId){
        return postService.likeStatusService(postId, extractUserId(jwtUserDetails));
    }

    // 댓글 달기
    @PostMapping("/community/board/{postId}/comment")
    public void postComments(@AuthenticationPrincipal JwtUserDetails jwtUserDetails, @PathVariable(value = "postId") Long postId,
                             @RequestBody CommentsRequestDTO.postComments postComments){
        commentsService.postComments(postId, extractUserId(jwtUserDetails), postComments);
    }

    // 게시글 좋아요
    @PostMapping("/community/comment/{commentId}/likes")
    public void likeComments(@AuthenticationPrincipal JwtUserDetails jwtUserDetails, @PathVariable(value = "commentId") Long commentId){
        commentsService.likeComments(commentId, extractUserId(jwtUserDetails));
    }

    // 댓글 달기
    @PostMapping("/community/comment/{commentId}")
    public void postCommentReply(@AuthenticationPrincipal JwtUserDetails jwtUserDetails, @PathVariable(value = "commentId") Long commentId, @RequestBody CommentsRequestDTO.postComments postComments){
        Long userId = jwtUserDetails.getUserId();
        commentsService.postReply(userId, commentId, postComments);
    }

    // 대댓글 달기
    @PostMapping("/community/reply/{replyId}/likes")
    public void likeReply(@AuthenticationPrincipal JwtUserDetails jwtUserDetails, @PathVariable(value = "replyId") Long replyId){
        commentsService.likeReply(replyId, extractUserId(jwtUserDetails));
    }

    private long extractUserId(JwtUserDetails jwtUserDetails) {
        return jwtUserDetails.getUserId();
    }
}
