package ru.practicum.shareit.user.controller;

import ru.practicum.shareit.user.dto.UserDTO;

import java.util.Collection;

/**
 * Интерфейс для контроллера по работе с пользователями
 */
public interface UserController {
    /**
     * Эндпоинт на добавление пользователя
     * @param newUser новый пользователь
     * @return объект созданного пользователя
     */
    UserDTO add(UserDTO newUser);

    /**
     * Эндпоинт на обновление данных о пользователе
     * @param userId идентификатор обновляемого пользователя
     * @param user новые данные о пользователе
     * @return объект обновленного пользователя
     */
    UserDTO update(Long userId, UserDTO user);

    /**
     * Эндпоинт получения конкретного пользователя
     *
     * @param id идентификатор пользователя
     * @return объект пользователя
     */
    UserDTO getUser(Long id);

    /** Эндпоинт удаления пользователя
     * @param userId идентфикатор пользователя
     */
    void deleteUser(Long userId);

    /**
     * Эндпоинт получения списка всех пользователей
     * @return список всех пользователей
     */
    Collection<UserDTO> getAllUsers();

}
