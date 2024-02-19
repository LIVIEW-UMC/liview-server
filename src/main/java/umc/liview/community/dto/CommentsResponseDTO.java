package umc.liview.community.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class CommentsResponseDTO {
    @Getter
    @Builder
    public static class GetComments{
        String name;
        String content;
        Long likes;
        boolean iLike;
        LocalDateTime time;
        List<GetReplies> replies;
    }

    @Getter
    @Builder
    public static class GetReplies{
        String name;
        String content;
        Long likes;
        boolean iLike;
        LocalDateTime time;
    }
}
