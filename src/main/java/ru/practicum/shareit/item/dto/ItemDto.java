package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO-объект для вещи
 */
@Data
@Builder
public class ItemDto {
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
    private boolean available;
}
