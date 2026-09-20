package ru.practicum.shareit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.dto.UserDTO;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;
import java.util.List;

@ActiveProfiles("test")
public class UserServiceTest {

    private User user1;
    private User user2;
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
        UserRepository userRepositoryMock = Mockito.mock(UserRepository.class);
        UserService userService = new UserServiceImpl(userRepositoryMock);

        Mockito
                .when(userRepositoryMock.findAll())
                .thenReturn(List.of(user1,user2));

        Collection<UserDTO> userDTOS = userService.getAllUsers();
        Assertions.assertEquals(2, userDTOS.size());
    }
}
