package umc.liview.tour.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.tour.domain.Tag;
import umc.liview.tour.domain.TourTags;

import javax.naming.Name;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findByName(String name);



}
