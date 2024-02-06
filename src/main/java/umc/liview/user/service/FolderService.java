package umc.liview.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.liview.user.domain.Folder;
import umc.liview.user.domain.FolderRepository;
import umc.liview.user.domain.User;
import umc.liview.user.domain.UserRepository;
import umc.liview.user.dto.FolderDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<FolderDTO> getMyFolder(Long userId, String owner) {
        //내 게시글이 담긴 나의 폴더
        List<FolderDTO> myFolderDTOList = new ArrayList<>();
        if (owner.equals("true")){
            List<Folder> myFolderList = folderRepository.getByUserIdAndOwner(userId, Folder.Owner.MINE);

            for (Folder folder : myFolderList){
                myFolderDTOList.add(FolderDTO.toFolderDTO(folder));
            }
         }
        // 상대 게시글이 담긴 나의 폴더
        else if (owner.equals("false")){
            List<Folder> myFolderList = folderRepository.getByUserIdAndOwner(userId, Folder.Owner.OTHERS);

            for (Folder folder : myFolderList){
                myFolderDTOList.add(FolderDTO.toFolderDTO(folder));
            }
        }

        return myFolderDTOList;

    }

    public void renameFolder(FolderDTO requestFolderDTO) {
        Folder folder = folderRepository.getById(requestFolderDTO.getId());
        folder.changeName(requestFolderDTO.getName());
        folderRepository.save(folder);
    }
}
