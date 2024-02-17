package umc.liview.community.controller.dto.request;

import lombok.Getter;

public record PostSearchDto(String searchValue, String sortedBy, int page) {
}
