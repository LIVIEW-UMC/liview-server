package umc.liview.user.dto;

import lombok.*;
import umc.liview.user.domain.Folder;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FolderDTO {

    private Long id;
    private String name;
    private Long userId;
    private Folder.Owner owner;
    private Folder.ActivationStatus activationStatus;


    public static FolderDTO toFolderDTO(Folder folder){
        return FolderDTO.builder()
                .id(folder.getId())
                .name(folder.getName())
                .owner(folder.getOwner())
                .activationStatus(folder.getActivationStatus())
                .build();
    }

}
