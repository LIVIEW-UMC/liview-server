package umc.liview.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.tour.domain.Tour;

public interface TourRepository extends JpaRepository<Tour,Long> {
}
