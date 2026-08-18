package ru.practicum.shareit.user.mapping;

import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;

public class UserMap {

    public static User userDTOToUser(UserDTO userDTO) {
        return User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .build();
    }

    public static UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
