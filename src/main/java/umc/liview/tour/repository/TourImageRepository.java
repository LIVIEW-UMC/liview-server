package umc.liview.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.tour.domain.TourImages;

import java.util.Optional;

public interface TourImageRepository extends JpaRepository<TourImages,Long> {

}
