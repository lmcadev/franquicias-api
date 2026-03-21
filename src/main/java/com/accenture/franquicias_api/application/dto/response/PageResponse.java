package com.accenture.franquicias_api.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO genérico para respuestas paginadas
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    @JsonProperty("content")
    private List<T> content;

    @JsonProperty("current_page")
    private Integer currentPage;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("total_elements")
    private Long totalElements;

    @JsonProperty("total_pages")
    private Integer totalPages;

    @JsonProperty("is_last")
    private Boolean isLast;

    public static <T> PageResponse<T> of(List<T> content, Integer page, Integer size, Long totalElements) {
        Integer totalPages = (int) Math.ceil((double) totalElements / size);
        Boolean isLast = page >= totalPages - 1;

        return PageResponse.<T>builder()
                .content(content)
                .currentPage(page)
                .pageSize(size)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .isLast(isLast)
                .build();
    }
}
