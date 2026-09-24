package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class ItemRequestServiceTest {

    @Autowired
    private ItemRequestService itemRequestService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    private User owner;
    private User otherUser;
    private ItemRequestDto requestDto;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setName("Тестов Тест");
        owner.setEmail("test@test.com");
        owner = userRepository.save(owner);
        otherUser = new User();
        otherUser.setName("Петров Петр");
        otherUser.setEmail("petrov@test.com");
        otherUser = userRepository.save(otherUser);
        requestDto = ItemRequestDto.builder()
                    .description("Нужна машина на выходные")
                .build();
    }

    @Test
    void testAddNewRequest() {
        ItemRequestDto result = itemRequestService.addNewRequest(requestDto, owner.getId());
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Нужна машина на выходные");
        ItemRequest saved = itemRequestRepository.findById(result.getId()).orElseThrow();
        assertThat(saved.getUser().getId()).isEqualTo(owner.getId());
    }

    @Test
    void testAddNewRequest_whenUserNotFound() {
        assertThrows(NotFoundException.class,
                () -> itemRequestService.addNewRequest(requestDto, 999L));
    }

    @Test
    void testAddNewRequest_whenDtoIsNull() {
        assertThrows(ValidationException.class,
                () -> itemRequestService.addNewRequest(null, owner.getId()));
    }

    @Test
    void testAddNewRequest_whenUserIdIsNull() {
        assertThrows(ValidationException.class,
                () -> itemRequestService.addNewRequest(requestDto, null));
    }

    @Test
    void testGetItemRequests_whenOnlyOwn() {
        itemRequestService.addNewRequest(requestDto, owner.getId());
        ItemRequestDto otherRequest = ItemRequestDto.builder()
                .description("Нужен стол")
                .build();
        itemRequestService.addNewRequest(otherRequest, otherUser.getId());
        List<ItemRequestDto> result = itemRequestService.getItemRequests(owner.getId(), false);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).isEqualTo("Нужна машина на выходные");
    }

    @Test
    void testGetItemRequests_whenAll() {
        itemRequestService.addNewRequest(requestDto, owner.getId());
        ItemRequestDto otherRequest = ItemRequestDto.builder()
                .description("Нужен стол")
                .build();
        itemRequestService.addNewRequest(otherRequest, otherUser.getId());
        List<ItemRequestDto> result = itemRequestService.getItemRequests(owner.getId(), true);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDescription()).isEqualTo("Нужен стол");
    }

    @Test
    void testGetItemRequests_whenUserNotFound() {
        assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequests(999L, false));
    }

    @Test
    void testGetItemRequests_whenUserIdIsNull() {
        assertThrows(ValidationException.class,
                () -> itemRequestService.getItemRequests(null, false));
    }

    @Test
    void testGetItemRequestById() {
        ItemRequestDto created = itemRequestService.addNewRequest(requestDto, owner.getId());
        ItemRequestDto result = itemRequestService.getItemRequestById(created.getId());
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(created.getId());
        assertThat(result.getDescription()).isEqualTo("Нужна машина на выходные");
    }

    @Test
    void testGetItemRequestById_whenNotFound() {
        assertThrows(ValidationException.class,
                () -> itemRequestService.getItemRequestById(999L));
    }

    @Test
    void testGetItemRequestById_whenIdIsNull() {
        assertThrows(ValidationException.class,
                () -> itemRequestService.getItemRequestById(null));
    }

    @Test
    void testGetItemRequestById_withAddedItems() {
        ItemRequestDto created = itemRequestService.addNewRequest(requestDto, owner.getId());
        Item item = new Item();
        item.setName("Стол");
        item.setDescription("Икеевский");
        item.setAvailable(true);
        item.setOwnerId(otherUser.getId());
        item.setRequestId(created.getId());
        itemRepository.save(item);
        ItemRequestDto result = itemRequestService.getItemRequestById(created.getId());
        assertThat(result).isNotNull();
    }
}