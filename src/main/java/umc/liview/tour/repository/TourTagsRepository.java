package umc.liview.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.domain.TourImages;
import umc.liview.tour.domain.TourTags;

import java.util.List;
import java.util.Optional;

public interface TourTagsRepository extends JpaRepository<TourTags,Long> {

    List<TourTags> findAllByTourId(Long tourId);

    void deleteAllByTourId(Long tourId);

}
