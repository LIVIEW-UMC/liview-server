package umc.liview.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import umc.liview.user.domain.Folder;
import umc.liview.user.dto.FolderDTO;
import umc.liview.user.service.FolderService;




@RestController
@RequiredArgsConstructor
@RequestMapping("/tours")
@Slf4j
public class FolderController {
    private final FolderService folderService;
    private static final Logger logger = LoggerFactory.getLogger(FolderController.class);


//    @GetMapping("/") //폴더 리스트 조회 API
//    public List<FolderDTO> FolderListController(
//            @RequestParam String owner){
//        // owner=true -> 내 일정이 담긴 폴더
//        if (owner.equals("true")){
//
//
//            return List<FolderDTO> ;
//        }
//        // owner=false -> 상대 일정이 담긴 폴더
//        else if(owner.equals("false")){
//
//
//
//            return List<FolderDTO> ;
//        }
//
//    }


//    Refactor 1. createFolder   인증후 user_id 넣어줘야 함. 현재 NULL
//             2. elif 밑에 else로 예외처리 해줘야함. False 같은 거 들어오면 예외로 해야해
//             3. 같은 이름이 중복되면 안된다고 예외처리도 해줘야 해. 안했을 시에 삭제,조회 시에 문제 생길 거 같아

    @PostMapping("/folder")
    public void createFolderController(
            @RequestParam String owner,
            @RequestBody FolderDTO tempfolderDTO) {


        // owner=true -> 내 일정이 담긴 폴더
        if (owner.equals("true")) {


            FolderDTO folderDTO = FolderDTO.builder()
                    .name(tempfolderDTO.getName())
                    .owner(Folder.Owner.MINE)
                    .activationStatus(Folder.ActivationStatus.ACTIVATED)
                    .build();


            folderService.createFolder(folderDTO);
            //logger.info(tempfolderDTO.getName());
        }
        // owner=false -> 상대 일정이 담긴 폴더
        else if (owner.equals("false")) {

            FolderDTO folderDTO = FolderDTO.builder()
                    .name(tempfolderDTO.getName())
                    .owner(Folder.Owner.OTHERS)
                    .activationStatus(Folder.ActivationStatus.ACTIVATED)
                    .build();

            folderService.createFolder(folderDTO);

        }

    }
}
