package umc.liview.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import umc.liview.user.domain.FolderRepository;
import umc.liview.user.dto.FolderDTO;
import umc.liview.user.service.FolderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tours/folder")
public class FolderController {
    private final FolderService folderService;

    @GetMapping("/")
    public List<FolderDTO> FolderListController(
            @RequestParam String owner){

    }



}
