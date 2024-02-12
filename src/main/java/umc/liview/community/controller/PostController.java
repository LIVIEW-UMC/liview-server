package umc.liview.community.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import umc.liview.community.service.PostService;
import umc.liview.config.auth.JwtUserDetails;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.dto.SimpleTourDTO;
import umc.liview.tour.service.TourService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final TourService tourService;
    // 게시글 공개, 비공개 수정
    @PatchMapping("/community/post/{postId}")
    public void togglePostController(@PathVariable Long postId){ postService.togglePostService(postId);}


    //미분류 게시물 조회
    @GetMapping("/community/post/{userId}")
    public List<SimpleTourDTO> getNonClassPostController(@PathVariable Long userId, @AuthenticationPrincipal JwtUserDetails jwtUserDetails ){

        Long myId = jwtUserDetails.getUserId();
        //내 거 조회 -> 전체
        if (myId.equals(userId)) {
            List<Tour> tourList = postService.getMyNonClassPost(myId);
            List<SimpleTourDTO> simpleTourDTOList = tourService.putImage(tourList);
            return simpleTourDTOList;
        }

        //상대거 조회 공개만 조회
        else{
            List<Tour> tourList = postService.getOtherNonClassPost(userId);
            List<SimpleTourDTO> simpleTourDTOList = tourService.putImage(tourList);
            return simpleTourDTOList;
        }


    }


}
