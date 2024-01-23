package umc.liview.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.liview.user.domain.Folder;
import umc.liview.user.domain.FolderRepository;
import umc.liview.user.dto.FolderDTO;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;

    public void createFolder(FolderDTO folderDTO) {
        Folder folder = Folder.toFolderEntity(folderDTO);

        folderRepository.save(folder);
    }
}
