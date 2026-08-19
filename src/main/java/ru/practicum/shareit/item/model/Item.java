package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;

/**
 * Модель данных вещи
 */
@Data
@Builder
public class Item {
    /**
     * Идентификатор вещи
     */
    private Long id;
    /**
     * Идентификатор владельца
     */
    private Long ownerId;
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
}
