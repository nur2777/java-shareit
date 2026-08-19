package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ClientErrorException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapping.UserMap;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    @Override
    public UserDTO getUser(Long userId) {
        return UserMap.userToUserDTO(userDAO.getUserById(userId));
    }

    @Override
    public UserDTO addNewUser(UserDTO userDTO) {
        User newUser = UserMap.userDTOToUser(userDTO);
        if (emailIsDuplicate(newUser.getEmail())) {
            throw new ClientErrorException(String.format("Пользователь с e-mail '{}' уже существует." +
                    "Создание пользователей с одинаковым Email запрещено!",newUser.getEmail()));
        }
        return UserMap.userToUserDTO(userDAO.createUser(newUser));
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Long userId) {
        User updatedUser = UserMap.userDTOToUser(userDTO);
        updatedUser.setUserId(userId);
        if (updatedUser.getEmail() != null) {
            if (!updatedUser.getEmail().contains("@")) {
                throw new ValidationException("Электронная почта должна содержать символ @");
            }
            if (emailIsDuplicate(updatedUser.getEmail())) {
                throw new ClientErrorException(String.format("Пользователь с e-mail '{}' уже существует." +
                        "Обновление пользователей с одинаковым Email запрещено!", updatedUser.getEmail()));
            }
        }
        return UserMap.userToUserDTO(userDAO.updateUser(updatedUser));
    }

    @Override
    public void deleteUser(Long userId) {
         userDAO.deleteUser(userId);
    }

    @Override
    public Collection<UserDTO> getAllUsers() {
        return userDAO.getUsers().stream()
                .map(UserMap::userToUserDTO)
                .toList();
    }

    /** Проверка на дубликат. Существования пользователя с таким же email
     * @param email - почта которую надо проверить
     * @return true - если пользователь с такой почтой уже есть, иначе false
     */
    private boolean emailIsDuplicate(String email) {
        List<User> sameUsers = userDAO.getUsers().stream()
                .filter(user -> user.getEmail().equals(email))
                .toList();
        if (!sameUsers.isEmpty()) {
            log.info("Дубликат");
            return true;
        } else {
            return false;
        }
    }
}
