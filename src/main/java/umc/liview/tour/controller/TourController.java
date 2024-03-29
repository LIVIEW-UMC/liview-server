package umc.liview.tour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.liview.community.service.PostService;
import umc.liview.config.auth.JwtUserDetails;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.dto.*;
import umc.liview.tour.service.TagService;
import umc.liview.tour.service.TourImageService;
import umc.liview.tour.service.TourService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TourController {

    private final TourService tourservice;
    private final TourImageService tourImageService;
    private final PostService postService;
    private final TagService tagService;

    // 일정 생성
    @PostMapping(value = "/tours")
    public void makeTourController(
            //제목, 내용, 완료여부
            @RequestPart TourRequestDTO tourRequestDTO,
            @RequestPart List<MultipartFile> multipartFileList,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
                ){

        Long userId = jwtUserDetails.getUserId();
        tourservice.makeTourService(tourRequestDTO, multipartFileList,tourRequestDTO.getImageMetadataDTOList(),userId);

    }



    @GetMapping("/community/postId/{tourId}")
    public Long getPostIdController(@AuthenticationPrincipal JwtUserDetails jwtUserDetails,@PathVariable Long tourId
    ){
        return tourservice.getPostIdService(tourId);
    }

    // 미완성 일정 리스트 조회
    @GetMapping("/tours/incompleted/simple")
    public List<SimpleTourDTO> getAllInCompletedTourController(
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ){
        Long userId = jwtUserDetails.getUserId();
        List<Tour> tourList = tourservice.getAllIncompletedTour(userId);
        List<SimpleTourDTO> simpleIncompletedTourDTOS = new ArrayList<>();

        if (!tourList.isEmpty()) {
            for(Tour tour : tourList){
                simpleIncompletedTourDTOS.add(
                SimpleTourDTO.builder()
                        .tourId(tour.getId())
                        .title(tour.getTitle())
                        .localDateTime(tour.getCreatedAt())
                        .size(tour.getSize())
                        .imageURL(tourImageService.getThumbnail(tour))
                        .build());
            }

        }
        return simpleIncompletedTourDTOS;
    }

    //미완성 일정 상세 조회
    @GetMapping("/tours/incompleted/detail/{tourId}")
    public DetailIncompletedTourDTO getIncompleteTourController(
            @PathVariable Long tourId){
        return tourservice.getDetailIncompletedTourDTO(tourId);
        }

    @DeleteMapping("/tours/{tourId}")
    public void deleteTourController(
            @PathVariable Long tourId
    ){
        tourservice.deleteTourService(tourId);
    }
}
