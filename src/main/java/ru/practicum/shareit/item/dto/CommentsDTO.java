package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

import static ru.practicum.shareit.constants.Constants.DATE_TIME_PATTERN;

@Data
public class CommentsDTO {
    /**
     * Идентификатор отзыва
     */
    private Long id;

    /**
     *  Текст отзыва
     */
    @NotNull(message = "Текст отзыва не может быть пустым")
    private String text;

    /**
     *  Имя автора отзыва
     */
    private String authorName;

    /**
     * Дата и время создания отзыва
     */
    @JsonFormat(pattern = DATE_TIME_PATTERN)
    private LocalDateTime created;
}
