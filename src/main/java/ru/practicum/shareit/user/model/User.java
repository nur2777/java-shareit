package ru.practicum.shareit.user.model;


import lombok.Builder;
import lombok.Data;

/**
 * Модель данных пользователя
 */
@Data
@Builder
public class User {
    /**
     * Идентификатор пользователя
     */
    private Long userId;
    /**
     * Имя пользователя
     */
    private String name;
    /**
     * Электронная почта пользователя
     */
    private String email;
}
