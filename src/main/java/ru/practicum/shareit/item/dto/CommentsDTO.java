package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
}
