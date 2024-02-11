package umc.liview.tour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.liview.config.auth.JwtUserDetails;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.domain.TourImages;
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
    private final TagService tagService;

    // 일정 생성
    @PostMapping(value = "/tours")
    public void makeTourController(
            //제목, 내용, 완료여부
            @RequestPart TourRequestDTO tourRequestDTO,
            @ModelAttribute ImageCreateModel imageCreateModel,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
                ){

        Long userId = jwtUserDetails.getUserId();
        tourservice.makeTourService(tourRequestDTO,imageCreateModel.getImageCreateDTOS(),userId);

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
                        .imageURL(tourImageService.getThumbnail(tour))
                        .build());
            }
            log.info(simpleIncompletedTourDTOS.get(0).getImageURL());
        }
        return simpleIncompletedTourDTOS;
    }

    //미완성 일정 상세 조회
    @GetMapping("/tours/incompleted/detail/{tourId}")
    public DetailIncompletedTourDTO getDetailIncompletedTourController(
            @PathVariable Long tourId){

        Tour tour = tourservice.getTour(tourId);

        List<TourImages> tourImagesList = new ArrayList<>();
        tourImagesList.add(tourImageService.getThumbnailDetail(tourId));
        tourImagesList.addAll(tourImageService.getNotThumbailDetail(tourId));

        return DetailIncompletedTourDTO.builder()
                .tourId(tourId)
                .contents(tour.getContents())
                .title(tour.getTitle())
                .hashtag(tagService.getHashtag(tourId))
                .imgList(tourImagesList)
                .build();
    }

    @DeleteMapping("/tours/{tourId}")
    public void deleteTourController(
            @PathVariable Long tourId
    ){
        tourservice.deleteTourService(tourId);
    }


}