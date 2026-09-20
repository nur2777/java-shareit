package ru.practicum.shareit.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * DTO для данных пользователя
 */
@Data
@Builder
public class UserDTO {
    /**
     * Идентификатор пользователя
     */
    private Long id;
    /**
     * Имя пользователя
     */
    private String name;
    /**
     * Электронная почта пользователя
     */
    private String email;
}
