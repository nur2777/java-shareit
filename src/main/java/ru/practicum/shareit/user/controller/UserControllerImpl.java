package ru.practicum.shareit.user.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;


@RestController
@Slf4j
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserControllerImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping
    @Override
    public UserDTO add(@Valid @RequestBody UserDTO newUser) {
        return userService.addNewUser(newUser);
    }

    @PatchMapping("/{userId}")
    @Override
    public UserDTO update(@Valid @PathVariable Long userId,
                          @RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO,userId);
    }

    @GetMapping("/{userId}")
    @Override
    public UserDTO getUser(@Valid @PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    @Override
    public void deleteUser(@Valid @PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping
    @Override
    public Collection<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }
}
