package umc.liview.community.service.dto.response;

public record PostInfo(long postId, String imgUrl) {
    public long getPostId() {
        return postId;
    }
}
