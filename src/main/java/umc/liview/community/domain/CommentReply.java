package umc.liview.community.domain;

import jakarta.persistence.*;
import lombok.*;
import umc.liview.user.domain.User;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class CommentReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "contents")
    private String contents;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comments_id")
    private Comments comments;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
