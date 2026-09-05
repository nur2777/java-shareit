package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * DTO-объект для вещи
 */
@Data
@Builder
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
}
