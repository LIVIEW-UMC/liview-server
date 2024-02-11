package umc.liview.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.liview.config.auth.JwtUserDetails;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.dto.SimpleTourDTO;
import umc.liview.tour.service.TourImageService;
import umc.liview.tour.service.TourService;
import umc.liview.user.domain.Folder;
import umc.liview.user.dto.FolderDTO;
import umc.liview.user.service.FolderService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tours")
@Slf4j
public class FolderController {
    private final FolderService folderService;
    private final TourImageService tourImageService;
    private final TourService tourService;

    //폴더 상세 조회
    @GetMapping("/folder/detail/{folderId}")
    public List<SimpleTourDTO> getFolderDetailController(
            @PathVariable Long folderId
    ){
        List<Tour> tourList = folderService.getFolderDetailService(folderId);
        List<SimpleTourDTO> simpleTourDTOList = new ArrayList<>();


        if (!tourList.isEmpty()) {
            for(Tour tour : tourList){
                simpleTourDTOList.add(
                        SimpleTourDTO.builder()
                                .tourId(tour.getId())
                                .title(tour.getTitle())
                                .localDateTime(tour.getCreatedAt())
                                .imageURL(tourImageService.getThumbnail(tour))
                                .build());
            }
        }
        return simpleTourDTOList;

    }

    @GetMapping("/folder") //폴더 리스트 조회 API
    public List<FolderDTO> FolderListController(
            @RequestParam String owner,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails) {

        Long userId = jwtUserDetails.getUserId();
        return folderService.getMyFolder(userId, owner);
    }

    @PostMapping("/folder/{folderId}/{tourId}")
    public void addTourController(
            @PathVariable Long folderId,
            @PathVariable Long tourId
    ){
        folderService.addTourService(tourId,folderId);
    }

    @DeleteMapping ("/folder/{folderId}/{tourId}")
    public void deleteTourController(
            @PathVariable Long folderId,
            @PathVariable Long tourId
    ){
        folderService.deleteTourService(tourId,folderId);
    }




//    Refactor 1. createFolder   인증후 user_id 넣어줘야 함. 현재 NULL
//             2. elif 밑에 else로 예외처리 해줘야함. False 같은 거 들어오면 예외로 해야해
//             3. 같은 유저가 같은 이름이 중복되면 안된다고 예외처리도 해줘야 해. 안했을 시에 삭제,조회 시에 문제 생길 거 같아
//             4. 유저가 없으면 ? 레파지토리에서 에러 날리는 거 예외처리도 해줘야해


    //폴더 생성
    @PostMapping("/folder")
    public void createFolderController(
            @RequestParam String owner,
            @RequestBody FolderDTO tempfolderDTO,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
            ) {

        Long userId = jwtUserDetails.getUserId();
        // owner=true -> 내 일정이 담긴 폴더
        if (owner.equals("true")) {

            FolderDTO folderDTO = FolderDTO.builder()
                    .name(tempfolderDTO.getName())
                    .userId(userId)
                    .owner(Folder.Owner.MINE)
                    .activationStatus(Folder.ActivationStatus.ACTIVATED)
                    .build();

            folderService.createFolder(folderDTO);

        }
        // owner=false -> 상대 일정이 담긴 폴더
        else if (owner.equals("false")) {

            FolderDTO folderDTO = FolderDTO.builder()
                    .name(tempfolderDTO.getName())
                    .userId(userId)
                    .owner(Folder.Owner.OTHERS)
                    .activationStatus(Folder.ActivationStatus.ACTIVATED)
                    .build();

            folderService.createFolder(folderDTO);
        }
    }

    @DeleteMapping("/folder/{folder_Id}")
    public void deleteFolderController(
            @PathVariable("folder_Id") Long folderId) {
        folderService.deleteFolderService(folderId);
    }

    @PatchMapping("/folder/rename")
    public void renameFolderController(
            @RequestBody FolderDTO requestFolderDTO,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ){
        Long userId = jwtUserDetails.getUserId();
        folderService.renameFolder(userId,requestFolderDTO);
    }
}
