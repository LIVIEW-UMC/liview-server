package umc.liview.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.liview.tour.domain.Tour;
import umc.liview.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface TourRepository extends JpaRepository<Tour,Long> {


    List<Tour> findAllByUserAndCompleteStatus(User user, Tour.CompleteStatus completeStatus);

    List<Tour> findAllByUserIdAndCompleteStatus(Long userId, Tour.CompleteStatus completeStatus);


    List<Tour> findAllByUserIdAndCompleteStatusAndIsClassified(Long userId, Tour.CompleteStatus completeStatus , boolean isClassfied);


}
