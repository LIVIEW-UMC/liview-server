package umc.liview.tour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import umc.liview.community.domain.Post;
import umc.liview.community.service.PostService;
import umc.liview.config.s3.AmazonS3Manager;
import umc.liview.tour.domain.Tag;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.domain.TourImages;
import umc.liview.tour.domain.TourTags;
import umc.liview.tour.dto.ImageMetadataDTO;
import umc.liview.tour.dto.SimpleTourDTO;
import umc.liview.tour.dto.TourRequestDTO;
import umc.liview.tour.repository.TagRepository;
import umc.liview.tour.repository.TourImageRepository;
import umc.liview.tour.repository.TourRepository;
import umc.liview.tour.repository.TourTagsRepository;
import umc.liview.user.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourService {
    private final TourTagsRepository tourTagsRepository;
    private final TagRepository tagRepository;
    private final TourRepository tourRepository;
    private final TourImageRepository tourImageRepository;
    private final AmazonS3Manager s3Manager;
    private final StoredToursRepository storedToursRepository ;
    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final PostService postService;
    private final TourImageService tourImageService;

    //임시 저장 일정 간단 조회
    public List<Tour> getAllIncompletedTour(Long userId){
        User user = userRepository.getReferenceById(userId);
        return tourRepository.findAllByUserAndCompleteStatus(user,Tour.CompleteStatus.INCOMPLETE);
    }


    //완성 일정 간단 조회
    public List<Tour> getAllCompletedTour(Long userId){
        User user = userRepository.getReferenceById(userId);
        return tourRepository.findAllByUserAndCompleteStatus(user,Tour.CompleteStatus.COMPLETE);

    }


    //폴더 저장 로직!
    // i) DTO가 Compelete 라면
    // ii) classfiedTour 실행 folder와 tour 기입
    public void classfiedTour(Tour tour, Folder folder)
    {
        storedToursRepository.save(StoredTours.builder()
                        .tourId(tour.getId())
                        .folder(folder)
                        .build());
    }

    public void createHashtag(Tour tour, List<String> tagNames){

        for(String hashtag : tagNames) {
            //해쉬태그 생성
            Optional<Tag> tempTag = tagRepository.findByName(hashtag);

            if (tempTag.isPresent()) {
                tourTagsRepository.save(TourTags.builder()
                        .tag(tempTag.get())
                        .tour(tour)
                        .build());

            } else {
                Tag tag = tagRepository.save(Tag.toTagEntity(hashtag));
                tourTagsRepository.save(TourTags.builder()
                        .tag(tag)
                        .tour(tour)
                        .build());

            }

        }
    }

    @Transactional
    public void deleteHashtag(Tour tour){
        tourTagsRepository.deleteAllByTourId(tour.getId());
    }


    // 폴더 선택하면 폴더에 넣어지도록 !!
    @Transactional
    public void makeTourService(TourRequestDTO tourRequestDTO, List<MultipartFile> multipartFileList, List<ImageMetadataDTO> imageMetadataDTOList, Long userId){

        User user = userRepository.getReferenceById(userId);



    if (tourRequestDTO.getTourId() != null){
        Optional<Tour> tourTemp = tourRepository.findById(tourRequestDTO.getTourId());
        Tour tour = tourTemp.get();

        tour.changeClassfied(Boolean.parseBoolean(tourRequestDTO.getIsClassfied()));
        tour.changeContent(tourRequestDTO.getContents());
        tour.changeTitle(tourRequestDTO.getTitle());
        tour.changeCompleteStatus(tourRequestDTO.getCompleteStatus());
        tour.changeSize(tourRequestDTO.getSize());
        tour.changeStartDay(tourRequestDTO.getStartDay());
        tour.changeEndDay(tourRequestDTO.getEndDay());

        if (tourRequestDTO.getCompleteStatus().equals(Tour.CompleteStatus.COMPLETE)) {
            Post post = postService.createPost(userId);
            tour.setPost(post);
        }

        //해시태그 수정도 해야 해
        deleteHashtag(tour);
        tourRepository.save(tour);
        createHashtag(tour,tourRequestDTO.getHashtag());

        int index = 0;
        // 투어 생성 -> for each 이미지 업로드 => Tourimage생성
        for (ImageMetadataDTO meta : imageMetadataDTOList) {

            String imgURL = s3Manager.uploadFile(multipartFileList.get(index));

            TourImages tourImages = TourImages.builder()
                    .imageUrl(imgURL)
                    .tour(tour)
                    .date(meta.getDate())
                    .isThumbnail(Boolean.parseBoolean(meta.getIsThumbnail()))
                    .latitude(meta.getLatitude())
                    .longitude(meta.getLongitude())
                    .imageLocation(meta.getImgLocation())
                    .build();

            index++;
            tourImageRepository.save(tourImages);
        }

    //임시저장 -> 생성
        if (tourRequestDTO.getIsClassfied().equals("true") && tourRequestDTO.getCompleteStatus().equals(Tour.CompleteStatus.COMPLETE)){
            Optional<Folder> folder = folderRepository.findById(tourRequestDTO.getFolderId());
            classfiedTour(tour,folder.get());
        }


    }
    else{ //바로 생성

    Tour tour = Tour.toTourEntity(tourRequestDTO);
    tour.setUser(user);

    if (tourRequestDTO.getCompleteStatus().equals(Tour.CompleteStatus.COMPLETE)) {
        Post post = postService.createPost(userId);
        tour.setPost(post);
    }
    tourRepository.save(tour);

    createHashtag(tour,tourRequestDTO.getHashtag());

    int index = 0;
    // 투어 생성 -> for each 이미지 업로드 => Tourimage생성
    for (ImageMetadataDTO meta : imageMetadataDTOList) {
        String imgURL = s3Manager.uploadFile(multipartFileList.get(index));

        TourImages tourImages = TourImages.builder()
                .imageUrl(imgURL)
                .tour(tour)
                .date(meta.getDate())
                .isThumbnail(Boolean.parseBoolean(meta.getIsThumbnail()))
                .latitude(meta.getLatitude())
                .longitude(meta.getLongitude())
                .imageLocation(meta.getImgLocation())
                .build();

        tourImageRepository.save(tourImages);
    }

        if (tourRequestDTO.getIsClassfied().equals("true") && tourRequestDTO.getCompleteStatus().equals(Tour.CompleteStatus.COMPLETE)){
            Optional<Folder> folder = folderRepository.findById(tourRequestDTO.getFolderId());
            classfiedTour(tour,folder.get());
        }

    }
    }
    @Transactional
    public Tour getTour(Long tourId) {
        return tourRepository.getReferenceById(tourId);
    }

    @Transactional
    public void deleteTourService(Long tourId) {
        tourRepository.deleteById(tourId);

        //투어와 관련된 모든 폴더 저장 정보 삭제
        List<StoredTours> storedTours = storedToursRepository.findAllByTourId(tourId);
        if (!storedTours.isEmpty()) { //저장된 폴더가 있다면 폴더안의 투어 정보도 삭제
            storedToursRepository.deleteAllByTourId(tourId);
        }

    }

    @Transactional
    public List<SimpleTourDTO> putImage(List<Tour> tourList){
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
}
