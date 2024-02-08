package umc.liview.tour.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "tag")
    private List<TourTags> tourTags = new ArrayList<>();


    public static Tag toTagEntity(String hashtag){
        return Tag.builder()
                .name(hashtag)
                .build();
    }

}
