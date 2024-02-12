package umc.liview.user.dto;

import lombok.*;
import umc.liview.user.domain.Folder;

@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class FolderDTO {

    private Long id;
    private String name;
    private Long userId;
    private Folder.Owner owner;


    public static FolderDTO toFolderDTO(Folder folder){
        return FolderDTO.builder()
                .id(folder.getId())
                .name(folder.getName())
                .owner(folder.getOwner())
                .build();
    }

}
