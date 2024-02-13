package umc.liview.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.community.domain.Post;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.repository.TourRepository;
import umc.liview.tour.service.TourService;
import umc.liview.user.domain.*;
import umc.liview.user.dto.FolderDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FolderService {
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final TourService tourService;
    private final StoredToursRepository storedToursRepository;
    //이건 포스트가 만들어 져야 할 수 있음
    public List<Tour> getMyFolderDetailService(Long folderId) {

        Optional<Folder> tempFolder = folderRepository.findById(folderId);
        List<Tour> tourList = new ArrayList<>();

        if (tempFolder.isPresent()) {
            Folder folder = tempFolder.get();
            List<StoredTours> storedToursList = folder.getStoredTours();

            if (folder.getOwner() == Folder.Owner.OTHERS) {
                for (StoredTours storedTours : storedToursList) {
                    Tour tempTour = tourRepository.getReferenceById(storedTours.getTourId());
                    Post post = tempTour.getPost();

                    if (post.getPostStatus() == Post.PostStatus.PUBLIC) {
                        tourList.add(tempTour);
                    }

                }

            }
            else{
                for (StoredTours storedTours : storedToursList) {
                    Tour tempTour = tourRepository.getReferenceById(storedTours.getTourId());
                    tourList.add(tempTour);
                }
            }
        }
        return tourList;
    }

    public List<Tour> getOtherFolderDetailService(Long folderId) {

        Optional<Folder> tempFolder = folderRepository.findById(folderId);
        List<Tour> tourList = new ArrayList<>();

        if (tempFolder.isPresent()) {
            Folder folder = tempFolder.get();
            List<StoredTours> storedToursList = folder.getStoredTours();

                for (StoredTours storedTours : storedToursList) {
                    Tour tempTour = tourRepository.getReferenceById(storedTours.getTourId());
                    Post post = tempTour.getPost();

                    if (post.getPostStatus() == Post.PostStatus.PUBLIC) {
                        tourList.add(tempTour);
                    }



               }

        }
        return tourList;
    }




    public void createFolder(FolderDTO folderDTO) {
        //이거 예외처리하자
        User user  = userRepository.getById(folderDTO.getUserId());

        Folder folder = Folder.builder()
                .user(user)
                .name(folderDTO.getName())
                .owner(folderDTO.getOwner())
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


    public void renameFolder(Long userId,FolderDTO requestFolderDTO) {
        Folder folder = folderRepository.getById(requestFolderDTO.getId());
        folder.changeName(requestFolderDTO.getName());
        folderRepository.save(folder);
    }

    public void addTourService(Long tourId, Long folderId) {
        Tour tour = tourRepository.getReferenceById(tourId);
        Folder folder = folderRepository.getReferenceById(folderId);

        tourService.classfiedTour(tour,folder);
    }

    @Transactional
    public void deleteTourService(Long tourId, Long folderId) {
        storedToursRepository.deleteByTourIdAndFolderId(tourId,folderId);
    }
}
