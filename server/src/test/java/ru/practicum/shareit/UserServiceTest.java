package ru.practicum.shareit;

import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.exception.ClientErrorException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.mapping.UserMap;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@Transactional
@SpringBootTest()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepository userRepositoryMock;
    private User user1;
    private User user2;
    private final UserService service;
    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setName("Тестов1 Тест1 Тестович1");
        user1.setEmail("test1@test.test");

        user2 = new User();
        user2.setName("Тестов2 Тест2 Тестович2");
        user2.setEmail("test2@test.test");
    }

    @Test
    public void testGetAllUsers() {
        UserService userService = new UserServiceImpl(userRepositoryMock);
        Mockito
                .when(userRepositoryMock.findAll())
                .thenReturn(List.of(user1,user2));

        Collection<UserDTO> userDTOS = userService.getAllUsers();
        Assertions.assertEquals(2, userDTOS.size());
    }

    @Test
    public void testGetUser() {
        UserService userService = new UserServiceImpl(userRepositoryMock);
        Mockito
                .when(userRepositoryMock.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(user1));
        UserDTO userDTO = userService.getUser(1L);
        assertThat(userDTO).isNotNull();
        assertThat(userDTO.getEmail().equals("test1@test.test"));
    }

    @Test
    public void testGetUser_whenUserNotExist() {
        UserService userService = new UserServiceImpl(userRepositoryMock);
        Mockito
                .when(userRepositoryMock.findById(100L))
                .thenThrow(new NotFoundException("Пользователь с id 100 не найден"));

        final NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class,
                () -> userService.getUser(100L));

        Assertions.assertEquals("Пользователь с id 100 не найден", notFoundException.getMessage());
    }

    @Test
    void testAddNewUser() {
        UserDTO result = service.addNewUser(UserMap.userToUserDTO(user1));
        MatcherAssert.assertThat(result.getId(), notNullValue());

        UserDTO userDTO = service.getUser(result.getId());

        MatcherAssert.assertThat(user1.getName(), equalTo(userDTO.getName()));
        MatcherAssert.assertThat(user1.getEmail(), equalTo(userDTO.getEmail()));
    }


    @Test
    void testAddNewUser_whenEmailIsDuplicate() {
        UserDTO result = service.addNewUser(UserMap.userToUserDTO(user1));
        User wrongUser = new User();
        wrongUser.setName("Петров Иван");
        wrongUser.setEmail("test1@test.test");

        assertThrows(ClientErrorException.class, () -> service.addNewUser(UserMap.userToUserDTO(wrongUser)));
    }

    @Test
    void testUpdateUser() {
        UserDTO result = service.addNewUser(UserMap.userToUserDTO(user1));
        UserDTO changedUser = UserDTO.builder()
                .name("Олег Соколов")
                .email("changed@test.test")
                .build();
        UserDTO updatedUser = service.updateUser(changedUser, result.getId());

        assertThat(updatedUser.getName()).isEqualTo("Олег Соколов");
        assertThat(updatedUser.getEmail()).isEqualTo("changed@test.test");
    }

    @Test
    void testUpdateUser_whenIncorrectEmail() {
        UserDTO result = service.addNewUser(UserMap.userToUserDTO(user1));
        UserDTO changedUser = UserDTO.builder()
                .name("Олег Соколов")
                .email("changed2test.test")
                .build();

        assertThrows(ValidationException.class, () -> service.updateUser(changedUser, result.getId()));
    }

    @Test
    void testDeleteUser() {
        UserDTO result = service.addNewUser(UserMap.userToUserDTO(user1));
        service.deleteUser(result.getId());
        assertThrows(NotFoundException.class, () -> service.getUser(result.getId()));
    }
}
