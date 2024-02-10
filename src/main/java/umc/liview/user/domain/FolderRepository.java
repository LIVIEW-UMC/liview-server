package umc.liview.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.liview.user.dto.FolderDTO;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder,Long> {

    List<Folder> getByUserIdAndOwner(Long userId, Folder.Owner owner);
}
