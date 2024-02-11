package umc.liview.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PrivacyStatus {
    @Column(name = "is_InActivated")
    private boolean isInActivated;
    @Column(name = "tours_AutoPosted")
    private boolean toursAutoPosted;

    public void handleEmailPrivacy(){
        this.isInActivated = !this.isInActivated;
    }
    public void handleBoardPrivacy(){
        this.toursAutoPosted = !this.toursAutoPosted;
    }
}
