package umc.liview.tour.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import umc.liview.tour.dto.ImageCreateDTO;
import umc.liview.tour.dto.ImageCreateModel;
import umc.liview.tour.dto.TourRequestDTO;
import umc.liview.tour.service.TourService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TourController {

    private final TourService tourservice;

    @PostMapping(value = "/tours")
    public void makeTourController(
            //제목, 내용, 완료여부
            @RequestPart TourRequestDTO tourRequestDTO,
            @ModelAttribute ImageCreateModel imageCreateModel
            //@RequestPart List<ImageCreateDTO> imageCreateDTOS
                                   ){

        tourservice.makeTourService(tourRequestDTO,imageCreateModel.getImageCreateDTOS());

    }
}
