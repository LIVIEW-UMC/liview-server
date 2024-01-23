package umc.liview.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.liview.user.domain.FolderRepository;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
}
