package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;

/**
 * Контроллер для функционала по работе с пользователями
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/users")
@Validated
public class UserController {

    private final UserClient userClient;

    /**
     * Эндпоинт на добавление пользователя
     * @param newUser новый пользователь
     * @return объект созданного пользователя
     */
    @PostMapping
    public ResponseEntity<Object>  add(@Valid @RequestBody UserDTO newUser) {
        return userClient.addNewUser(newUser);
    }

    /**
     * Эндпоинт на обновление данных о пользователе
     * @param userId идентификатор обновляемого пользователя
     * @param userDTO новые данные о пользователе
     * @return объект обновленного пользователя
     */
    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Valid @PathVariable("userId") Long userId,
                                         @RequestBody UserDTO userDTO) {
        return userClient.updateUser(userDTO,userId);
    }

    /**
     * Эндпоинт получения конкретного пользователя
     *
     * @param userId идентификатор пользователя
     * @return объект пользователя
     */
    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@Valid @PathVariable Long userId) {
        return userClient.getUser(userId);
    }

    /** Эндпоинт удаления пользователя
     * @param userId идентфикатор пользователя
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@Valid @PathVariable Long userId) {
        return userClient.deleteUser(userId);
    }

    /**
     * Эндпоинт получения списка всех пользователей
     * @return список всех пользователей
     */
    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

}
