package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentsDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@Transactional
@SpringBootTest()
public class ItemServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private BookingRepository bookingRepository;
    private ItemDTO itemDTO;
    private ItemDTO itemDTO2;
    private User user;

    @BeforeEach
    void setUp() {
        itemDTO = new ItemDTO();
        itemDTO.setName("Автомобиль");
        itemDTO.setAvailable(true);
        itemDTO.setDescription("Кроссовер Gelly");

        user = new User();
        user.setName("Тестов Тест");
        user.setEmail("test@test.com");
        user = userRepository.save(user);

        itemDTO2 = new ItemDTO();
        itemDTO2.setAvailable(true);
        itemDTO2.setName("Шкаф");
        itemDTO2.setDescription("Для верхней одежды");
    }

    @Test
    void testCreateItem() {
        ItemDTO createdItem = itemService.createItem(itemDTO, user.getId());
        assertThat(createdItem.getId()).isNotNull();
        assertThat(createdItem.getName()).isEqualTo(itemDTO.getName());
    }

    @Test
    void testUpdate() {
        ItemDTO createdItemDto = itemService.createItem(itemDTO, user.getId());
        ItemDTO changedData = new ItemDTO();
        changedData.setName("Грузовик");
        changedData.setDescription("Камаз 5го поколения");
        ItemDTO updatedItem = itemService.updateItem(createdItemDto.getId(), changedData, user.getId());
        assertThat(updatedItem.getName()).isEqualTo("Грузовик");
        assertThat(updatedItem.getDescription()).isEqualTo("Камаз 5го поколения");
    }

    @Test
    void testDeleteItem() {
        ItemDTO createdItemDto = itemService.createItem(itemDTO, user.getId());
        itemService.deleteItem(createdItemDto.getId());
        assertThrows(NotFoundException.class, () -> itemService.getItem(createdItemDto.getId(),user.getId()));
    }

    @Test
    void testGetItem() {
        ItemDTO createdItemDto = itemService.createItem(itemDTO, user.getId());
        ItemDTO result = itemService.getItem(createdItemDto.getId(),user.getId());
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(itemDTO.getName());
    }

    @Test
    void testGetAllOwnerItems() {
        itemService.createItem(itemDTO, user.getId());
        itemService.createItem(itemDTO2, user.getId());
        Collection<ItemDTO> itemDTOS = itemService.getAllOwnerItems(user.getId());
        Assertions.assertEquals(2, itemDTOS.size());
    }

    @Test
    void testFindByNameDescription() {
        itemService.createItem(itemDTO, user.getId());
        itemService.createItem(itemDTO2, user.getId());

        Collection<ItemDTO> userDTOS = itemService.findByNameDescription(itemDTO.getName());
        Assertions.assertEquals(1, userDTOS.size());
    }

    @Test
    void testAddCommentToItem() {
        ItemDTO createdItemDto = itemService.createItem(itemDTO, user.getId());

        User booker = new User();
        booker.setName("Арендатор");
        booker.setEmail("booker@test.com");
        booker = userRepository.save(booker);

        Item item = itemRepository.findById(createdItemDto.getId()).orElseThrow();

        Booking booking = new Booking();
        booking.setBookingStart(LocalDateTime.now().minusDays(5));
        booking.setBookingEnd(LocalDateTime.now().minusDays(3));
        booking.setStatus("APPROVED");
        booking.setItem(item);
        booking.setUser(booker);
        bookingRepository.save(booking);

        CommentsDTO commentsDTO = new CommentsDTO();
        commentsDTO.setText("Тестовый комментарий");
        itemService.addCommentToItem(createdItemDto.getId(), commentsDTO, booker.getId());

        ItemDTO result = itemService.getItem(createdItemDto.getId(), user.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(createdItemDto.getId());
        assertThat(result.getComments()).hasSize(1);
        assertThat(result.getComments().get(0).getText()).isEqualTo("Тестовый комментарий");
    }

    @Test
    void testAddCommentToItem_whenValidationException() {
        ItemDTO createdItemDto = itemService.createItem(itemDTO, user.getId());
        CommentsDTO commentsDTO = new CommentsDTO();
        commentsDTO.setAuthorName("Петров Петр");
        commentsDTO.setText("Тестовый комментарий");
        assertThrows(ValidationException.class, () -> itemService.addCommentToItem(createdItemDto.getId(),commentsDTO,user.getId()));
    }
}
