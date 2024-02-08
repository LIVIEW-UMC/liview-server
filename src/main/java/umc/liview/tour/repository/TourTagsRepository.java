package umc.liview.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.tour.domain.TourImages;
import umc.liview.tour.domain.TourTags;

import java.util.Optional;

public interface TourTagsRepository extends JpaRepository<TourTags,Long> {

}
