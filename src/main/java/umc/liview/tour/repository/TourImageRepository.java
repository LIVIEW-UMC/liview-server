package umc.liview.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.domain.TourImages;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public interface TourImageRepository extends JpaRepository<TourImages,Long> {

    TourImages findByTourAndIsThumbnail(Tour tour ,boolean isThumbnail);
    List<TourImages> findAllByTourIdAndIsThumbnail(Long tourId , boolean isThumbnail);
    Optional<TourImages> findByTourIdAndIsThumbnail(Long tourId ,boolean isThumbnail);
}
