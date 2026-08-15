package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserDAO {

    /** Создание пользователя
     * @param user данные о пользователе
     * @return объект созданного пользователя
     */
    User createUser(User user);

    /** Обновление пользователя
     * @param user измененные данные
     * @return обновлённый пользователь
     */
    User updateUser(User user);

    /** Удаление пользователя
     * @param userId идентификатор удаляемого пользователя
     */
    void deleteUser(Long userId);

    /** Получение пользователя по ID
     * @param userId идентификатор пользователя
     * @return объект пользователя
     */
    User getUserById(Long userId);

    /** Метод получения списка всех пользователей
     * @return список пользователей
     */
    Collection<User> getUsers();
}
