package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

/**
 * Класс реализует хранение данных о пользователе в памяти приложения
 */
@Component("InMemoryUserImpl")
@Slf4j
public class InMemoryUserImpl implements UserDAO {

    private final Map<Long, User> users = new HashMap<>();

    /** Получаем следующий уникальный идентификатор
     * @return новый идентификатор пользователя
     */
    private long getNextUserId() {
        long currUserId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        currUserId++;
        return currUserId;
    }

    @Override
    public User createUser(User newUser) {
        newUser.setId(getNextUserId());
        users.put(newUser.getId(),newUser);
        log.info("Пользователь {} успешно добавлен c идентификатором {}.", newUser.getName(),newUser.getId());
        return newUser;
    }

    @Override
    public User updateUser(User updatedUser) {
        if (users.containsKey(updatedUser.getId())) {
            User user  = users.get(updatedUser.getId());
            if (updatedUser.getName() != null && !updatedUser.getName().isEmpty()) {
                user.setName(updatedUser.getName());
                log.info("Успешно обновлено имя пользователя на {}.", updatedUser.getName());
            }
            if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                user.setEmail(updatedUser.getEmail());
                log.info("Успешно обновлен E-mail пользователя на {}.", updatedUser.getEmail());
            }
            return user;
        } else {
            throw new NotFoundException("Не найден пользователь с идентификатором " + updatedUser.getId());
        }
    }

    @Override
    public void deleteUser(Long userId) {
        if (users.containsKey(userId)) {
            users.remove(userId);
            log.info("Пользователь c идентификатором {} успешно удален.", userId);
        } else {
            throw new NotFoundException("Не найден пользователь с идентификатором " + userId);
        }
    }

    @Override
    public User getUserById(Long userId) {
        if (users.containsKey(userId)) {
            return users.get(userId);
        } else {
            throw new NotFoundException("Не найден пользователь с идентификатором " + userId);
        }
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }
}
