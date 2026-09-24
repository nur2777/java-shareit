package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.constants.Constants.DATE_TIME_PATTERN;

/**
 * DTO-объект для запроса вещи
 */
@Data
@Builder
public class ItemRequestDto {
    /**
     * Идентификатор запроса
     */
    private Long id;
    /**
     *  Описание запроса
     */
    private String description;
    /**
     * Дата и время создания запроса
     */
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime created;

    /**
     * Список вещей добавленных в ответ на запрос
     */
    private List<RequestItemDTO> items;
}
