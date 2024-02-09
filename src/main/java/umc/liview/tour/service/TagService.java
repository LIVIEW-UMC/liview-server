package umc.liview.tour.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.tour.domain.Tag;
import umc.liview.tour.domain.TourTags;
import umc.liview.tour.repository.TagRepository;
import umc.liview.tour.repository.TourTagsRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TourTagsRepository tourTagsRepository;
    private final TagRepository tagRepository;

    @Transactional
    public List<String> getHashtag(Long tourId){
        List<TourTags> tourTagsList = tourTagsRepository.findAllByTourId(tourId);

        List<String> hashtagList = new ArrayList<>();

        for ( TourTags tag : tourTagsList){

            Tag hashtag = tagRepository.getReferenceById(tag.getTag().getId());
            hashtagList.add(hashtag.getName());
            log.info(hashtag.getName());
        }
        return hashtagList;

    }

}
