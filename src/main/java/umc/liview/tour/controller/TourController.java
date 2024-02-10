package umc.liview.tour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
            @ModelAttribute ImageCreateModel imageCreateModel
                ){

        Long userId = 1L;
        tourservice.makeTourService(tourRequestDTO,imageCreateModel.getImageCreateDTOS(),userId);

    }

    // 임시 저장 일정 간단 조회
    @GetMapping("/tours/incompleted/simple")
    public List<SimpleIncompletedTourDTO> getAllInCompletedTourController(){
        Long userId = 1L;
        List<Tour> tourList = tourservice.getAllIncompletedTour(userId);
        List<SimpleIncompletedTourDTO> simpleIncompletedTourDTOS = new ArrayList<>();

        if (!tourList.isEmpty()) {
            for(Tour tour : tourList){
                simpleIncompletedTourDTOS.add(
                SimpleIncompletedTourDTO.builder()
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

    @GetMapping("/tours/incompleted/{tourId}")
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






}
