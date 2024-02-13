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
    @GetMapping("/folder/detail/{folderId}/{userId}")
    public List<SimpleTourDTO> getFolderDetailController(
            @PathVariable Long folderId,
            @PathVariable Long userId,
            @AuthenticationPrincipal JwtUserDetails jwtUserDetails
    ){
        Long myId = jwtUserDetails.getUserId();

        if (myId.equals(userId)) {

            List<Tour> tourList = folderService.getMyFolderDetailService(folderId);
            List<SimpleTourDTO> simpleTourDTOList = tourService.putImage(tourList);
            return simpleTourDTOList;

        }
        else {
            List<Tour> tourList = folderService.getOtherFolderDetailService(folderId);
            List<SimpleTourDTO> simpleTourDTOList = tourService.putImage(tourList);
            return simpleTourDTOList;
        }

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
                    .build();

            folderService.createFolder(folderDTO);

        }
        // owner=false -> 상대 일정이 담긴 폴더
        else if (owner.equals("false")) {

            FolderDTO folderDTO = FolderDTO.builder()
                    .name(tempfolderDTO.getName())
                    .userId(userId)
                    .owner(Folder.Owner.OTHERS)
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
