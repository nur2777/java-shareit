package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserDTO;

import java.util.Collection;

/**
 * Интерфейс реализует основных CRUD-операций
 */
public interface UserService {

    /** Метод получения пользователя
     * @param userId идентификатор пользователя
     * @return объект пользователя
     */
    UserDTO getUser(Long userId);

    /** Метод добавления нового пользователя
     * @param userDTO данные нового пользователя
     * @return объект нового пользователя
     */
    UserDTO addNewUser(UserDTO userDTO);

    /** Метод обновления данных о пользователе
     * @param userDTO данные нового пользователя
     * @param userId идентификатор пользователя
     * @return объект обновленного пользователя
     */
    UserDTO updateUser(UserDTO userDTO, Long userId);

    /** Метод удаления пользователя
     * @param userId идентификатор пользователя
     */
    void deleteUser(Long userId);

    /** Метод получения списка всех пользователей
     * @return список всех пользователей
     */
    Collection<UserDTO> getAllUsers();
}
