package umc.liview.community.converter;

import umc.liview.community.domain.CommentReply;
import umc.liview.community.domain.Comments;
import umc.liview.community.domain.Post;
import umc.liview.user.domain.User;

import java.util.ArrayList;

public class CommentsConverter {
    public static Comments makeComments(User user, Post post, String contents){
        return Comments.builder()
                .contents(contents)
                .post(post)
                .user(user)
                .commentReply(new ArrayList<>())
                .build();
    }

    public static CommentReply makeReply(User user, Comments comments, String contents){
        return CommentReply.builder()
                .comments(comments)
                .contents(contents)
                .user(user)
                .build();
    }
}
