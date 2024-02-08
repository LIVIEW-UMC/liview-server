package umc.liview.tour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import umc.liview.aws.s3.AmazonS3Manager;
import umc.liview.aws.s3.Uuid;
import umc.liview.aws.s3.UuidRepository;
import umc.liview.tour.domain.Tag;
import umc.liview.tour.domain.Tour;
import umc.liview.tour.domain.TourImages;
import umc.liview.tour.domain.TourTags;
import umc.liview.tour.dto.ImageCreateDTO;
import umc.liview.tour.dto.TourRequestDTO;
import umc.liview.tour.repository.TagRepository;
import umc.liview.tour.repository.TourImageRepository;
import umc.liview.tour.repository.TourRepository;
import umc.liview.tour.repository.TourTagsRepository;
import umc.liview.user.domain.Folder;
import umc.liview.user.domain.StoredTours;
import umc.liview.user.domain.StoredToursRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public void makeTourService(TourRequestDTO tourRequestDTO, List<ImageCreateDTO> imageCreateDTOS){

        Tour tour = Tour.toTourEntity(tourRequestDTO);
        tourRepository.save(tour);
        createHashtag(tour,tourRequestDTO.getHashtag());

        // 투어 생성 -> for each 이미지 업로드 => Tourimage생성
        for (ImageCreateDTO img : imageCreateDTOS) {
            String imgURL = s3Manager.uploadFile(img.getFile());

                 TourImages tourImages = TourImages.builder()
                    .imageUrl(imgURL)
                    .tour(tour)
                    .date(img.getDate())
                    .isThumbnail(Boolean.parseBoolean(img.getIsThumbnail()))
                    .latitude(img.getLatitude())
                    .longitude(img.getLongitude())
                    .imageLocation(img.getImgLocation())
                    .build();

            tourImageRepository.save(tourImages);

            // 파일과 메타데이터 처리 로직
        }


    }
}
