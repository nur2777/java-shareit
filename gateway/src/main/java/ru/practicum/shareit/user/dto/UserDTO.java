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
    @NotNull(message = "Имя пользователя должно быть указано")
    @NotBlank(message = "Имя пользователя не может быть пустым")
    private String name;
    /**
     * Электронная почта пользователя
     */
    @NotNull(message = "Эл. почта пользователя должна быть указана")
    @NotBlank(message = "Эл. почта пользователя не может быть пустой")
    @Email(message = "Электронная почта должна содержать символ @")
    private String email;
}
