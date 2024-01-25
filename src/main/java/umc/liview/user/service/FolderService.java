package umc.liview.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.liview.user.domain.Folder;
import umc.liview.user.domain.FolderRepository;
import umc.liview.user.domain.User;
import umc.liview.user.domain.UserRepository;
import umc.liview.user.dto.FolderDTO;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    public void createFolder(FolderDTO folderDTO) {
        //이거 예외처리하자
        User user  = userRepository.getById(folderDTO.getUserId());

        Folder folder = Folder.builder()
                .user(user)
                .name(folderDTO.getName())
                .owner(folderDTO.getOwner())
                .activationStatus(folderDTO.getActivationStatus())
                .build();

        folderRepository.save(folder);
    }

    public void deleteFolderService(Long folderId) {
        folderRepository.deleteById(folderId);
    }
}
