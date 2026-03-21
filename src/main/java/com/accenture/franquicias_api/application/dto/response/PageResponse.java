package com.accenture.franquicias_api.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO genérico para respuestas paginadas de listas en la API.
 *
 * <p>
 * Tipo genérico &lt;T&gt; que envuelve cualquier tipo de contenido paginado.
 * Se utiliza en endpoints que retornan listas de recursos con paginación
 * (franchises, branches, products).
 * </p>
 *
 * <p>
 * Estructura de respuesta:
 * <ul>
 *   <li>content: Lista de elementos del tipo T</li>
 *   <li>current_page: Número de página actual (0-indexed)</li>
 *   <li>page_size: Cantidad de elementos en la página</li>
 *   <li>total_elements: Total de elementos disponibles</li>
 *   <li>total_pages: Total de páginas calculado</li>
 *   <li>is_last: Flag indicando si es la última página</li>
 * </ul>
 * </p>
 *
 * <p>
 * Ejemplo:
 * <pre>
 * {
 *   "content": [...],
 *   "current_page": 0,
 *   "page_size": 10,
 *   "total_elements": 45,
 *   "total_pages": 5,
 *   "is_last": false
 * }
 * </pre>
 * </p>
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
