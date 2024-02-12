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
    @Column(name = "enmail_reception_status")
    private boolean emailReceptionStatus;
    @Column(name = "tours_AutoPosted")
    private boolean toursAutoPosted;

    public void handleEmailPrivacy(){
        emailReceptionStatus = !emailReceptionStatus;
    }
    public void handleBoardPrivacy(){
        toursAutoPosted = !toursAutoPosted;
    }
}
