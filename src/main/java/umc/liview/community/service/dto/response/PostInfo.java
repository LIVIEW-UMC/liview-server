package umc.liview.community.service.dto.response;

public record PostInfo(long postId, long tourId, String imgUrl) {
    public long getPostId() {
        return postId;
    }
}
