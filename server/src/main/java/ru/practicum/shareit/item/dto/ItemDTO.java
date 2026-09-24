package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.constants.Constants.DATE_TIME_PATTERN;

/**
 * DTO-объект для вещи
 */
@Data
@NoArgsConstructor
public class ItemDTO {
    /**
     * Идентификатор вещи
     */
    private Long id;
    /**
     *  Короткое имя
     */
    private String name;
    /**
     * Описание
     */
    private String description;
    /**
     * Доступность вещи
     * True - доступна, False - не доступна
     */
    private Boolean available;
    /**
     * Комментарии к вещи
     */
    private List<CommentsDTO> comments;
    /**
     * Дата и время последнего бронирования
     */
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime lastBooking;
    /**
     * Дата и время ближайшего следующего бронирования
     */
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime nextBooking;
    /**
     * Идентификатор запроса на добавление вещи
     */
    private Long requestId;
}
