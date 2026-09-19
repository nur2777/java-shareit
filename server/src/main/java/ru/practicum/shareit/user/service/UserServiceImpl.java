package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ClientErrorException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapping.UserMap;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    public static final int PAGE_SIZE = 32;
    private final UserRepository userRepository;

    @Override
    public UserDTO getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден "));
        return UserMap.userToUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO addNewUser(UserDTO userDTO) {
        User newUser = UserMap.userDTOToUser(userDTO);
        if (emailIsDuplicate(newUser.getEmail())) {
            throw new ClientErrorException(String.format("Пользователь с e-mail '{}' уже существует." +
                    "Создание пользователей с одинаковым Email запрещено!",newUser.getEmail()));
        }
        return UserMap.userToUserDTO(userRepository.save(newUser));
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserDTO userDTO, Long userId) {
        User updatedUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден "));;
        if (userDTO.getName() != null) {
            updatedUser.setName(userDTO.getName());
        }
        if (userDTO.getEmail() != null) {
            updatedUser.setEmail(userDTO.getEmail());
        }
        if (updatedUser.getEmail() != null) {
            if (!updatedUser.getEmail().contains("@")) {
                throw new ValidationException("Электронная почта должна содержать символ @");
            }
        }
        return UserMap.userToUserDTO(userRepository.save(updatedUser));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public Collection<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMap::userToUserDTO)
                .toList();
    }

    /** Проверка на дубликат. Существования пользователя с таким же email
     * проверку выполняем постранично, что бы не выполнять полный селект из БД
     * @param email - почта которую надо проверить
     * @return true - если пользователь с такой почтой уже есть, иначе false
     */
    private boolean emailIsDuplicate(String email) {
        Sort sortById = Sort.by(Sort.Direction.ASC, "id");
        Pageable page = PageRequest.of(0, PAGE_SIZE, sortById);
        do {
            Page<User> userPage = userRepository.findAll(page);
            List<User> sameUsers = userPage.getContent()
                    .stream()
                    .filter(user -> user.getEmail().equals(email))
                    .toList();
            if (!sameUsers.isEmpty()) {
                log.info("Дубликат");
                return true;
            }
            if (userPage.hasNext()) {
                page = PageRequest.of(userPage.getNumber() + 1, userPage.getSize(), userPage.getSort());
            } else {
                page = null;
            }
        } while (page != null);
        return false;
    }
}
