package umc.liview.tour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.domain.TourImages;
import umc.liview.tour.repository.TourImageRepository;
import umc.liview.tour.repository.TourRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class TourImageService {
    private final TourImageRepository tourImageRepository;
    private final TourRepository tourRepository;

    @Transactional
    public String getThumbnail(Tour tour){
        TourImages tourImages = tourImageRepository.findByTourAndIsThumbnail(tour,true);
        return tourImages.getImageUrl();
    }

    @Transactional
    public TourImages getThumbnailDetail(Long tourId){
        Optional<TourImages> tourImages = tourImageRepository.findByTourIdAndIsThumbnail(tourId,true);
        Tour tour = tourRepository.getReferenceById(tourId);

        if (tour.getCompleteStatus() == Tour.CompleteStatus.INCOMPLETE) {
            tourImageRepository.delete(tourImages.get());
        }
        return tourImages.get();
    }
    @Transactional
    public List<TourImages> getNotThumbailDetail(Long tourId){
        List<TourImages> tourImagesList = tourImageRepository.findAllByTourIdAndIsThumbnail(tourId,false);
        Tour tour = tourRepository.getReferenceById(tourId);
        if (tour.getCompleteStatus() == Tour.CompleteStatus.INCOMPLETE) {
            tourImageRepository.deleteAll(tourImagesList);
        }
        return tourImagesList;

    }

}
