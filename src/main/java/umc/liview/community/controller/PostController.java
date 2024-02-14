package umc.liview.community.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.liview.community.domain.Post;
import umc.liview.community.dto.PostDTO;
import umc.liview.community.service.PostService;
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

@RestController
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;
    private final TourService tourService;
    private final TourImageService tourImageService;
    private final TagService tagService;
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

    @GetMapping("/community/post")
    public DetailIncompletedTourDTO getDetailPostController(
            @RequestParam Long tourId){

        Tour tour = tourService.getTour(tourId);

        Post post = tour.getPost();
        List<TourImages> tourImagesList = new ArrayList<>();
        tourImagesList.add(tourImageService.getThumbnailDetail(tourId));
        tourImagesList.addAll(tourImageService.getNotThumbailDetail(tourId));
        postService.increaseViewCount(post);

        return DetailIncompletedTourDTO.builder()
                .tourId(tourId)
                .contents(tour.getContents())
                .title(tour.getTitle())
                .hashtag(tagService.getHashtag(tourId))
                .imgList(tourImagesList)
                .build();
    }

    @GetMapping("/community/mypost")
    public List<PostDTO>getMyAllPostController(@AuthenticationPrincipal JwtUserDetails jwtUserDetails){

        Long myId = jwtUserDetails.getUserId();
        return postService.getMyAllPostService(myId);


    }



}
