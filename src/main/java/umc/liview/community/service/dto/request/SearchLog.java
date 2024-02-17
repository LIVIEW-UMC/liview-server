package umc.liview.community.service.dto.request;

import java.time.LocalDateTime;

public record SearchLog(String name, String createdAt) {
    public SearchLog(String name) {
        this(name, LocalDateTime.now().toString());
    }
}
