package umc.liview.community.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.liview.community.domain.Post;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "user_id")
    private long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
