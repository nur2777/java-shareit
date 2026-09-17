package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "Имя вещи должно быть указано")
    @NotBlank(message = "Имя вещи не может быть пустым")
    private String name;
    /**
     * Описание
     */
    @NotNull(message = "Описание вещи должно быть указано")
    @NotBlank(message = "Описание вещи не может быть пустым")
    private String description;
    /**
     * Доступность вещи
     * True - доступна, False - не доступна
     */
    @NotNull(message = "Статус доступности вещи должен быть указан")
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
}
