package umc.liview.user.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import umc.liview.common.BaseTimeEntity;
import umc.liview.common.FolderTimeEntity;
import umc.liview.user.dto.FolderDTO;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
@AllArgsConstructor
public class Folder extends FolderTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "owner")
    private Owner owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @RequiredArgsConstructor
    public enum Owner {
        MINE("나의_일정"),
        OTHERS("다른_회원_일정"),
        ;

        private final String ownerStatus;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "activation_status")
    private ActivationStatus activationStatus;


    @Getter
    @RequiredArgsConstructor
    public enum ActivationStatus {
        ACTIVATED("활성화"),
        INACTIVATED("비활성화"),
        ;

        private final String activeStatus;
    }


    public void toggleActivationStatus(){
        if(this.getActivationStatus() == ActivationStatus.ACTIVATED){
            this.activationStatus = ActivationStatus.INACTIVATED ;
        }
        else{
            this.activationStatus = ActivationStatus.ACTIVATED ;
        }
    }
        public static Folder toFolderEntity(FolderDTO folderDTO){
        return Folder.builder()
                .name(folderDTO.getName())
                .owner(folderDTO.getOwner())
                .activationStatus(folderDTO.getActivationStatus())
                .build();
    }


}
