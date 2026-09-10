package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Модель данных вещи
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "items")
public class Item {
    /**
     * Идентификатор вещи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * Идентификатор владельца
     */
    @Column(name = "owner_id", nullable = false)
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
