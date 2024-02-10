package umc.liview.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoredToursRepository extends JpaRepository<StoredTours,Long> {

    List<StoredTours> findAllByTourId(Long tourId);
    void deleteAllByTourId(Long tourId);

    void deleteByTourIdAndFolderId(Long tourId, Long folderId);
}
