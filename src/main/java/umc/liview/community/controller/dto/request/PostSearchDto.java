package umc.liview.community.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record PostSearchDto(
        @NotNull(message = "searchValue is null") String searchValue,
        @NotNull(message = "sortedBy is null") String sortedBy) {
}
