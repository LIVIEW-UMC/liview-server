package umc.liview.community.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import umc.liview.community.domain.Post;
import umc.liview.tour.domain.Tour;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

    Post findByTour(Tour tour);

}
