package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.StateEnum;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusEnum;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.CommentRepository;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.CommentsDTO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapping.CommentMap;
import ru.practicum.shareit.item.mapping.ItemMap;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDTO createItem(ItemDTO itemDTO, Long ownerId) {
        if (ownerId == null) {
            throw new ValidationException("При создании вещи не указан его владелец");
        }
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + ownerId + " не найден "));
        Item item = ItemMap.itemDTOToItem(itemDTO);
        item.setOwnerId(ownerId);
        return ItemMap.itemToItemDTO(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDTO updateItem(Long itemId, ItemDTO itemDTO,  Long ownerId) {
        if (ownerId == null) {
            throw new ValidationException("При обновлении вещи не указан его владелец");
        }
        User user = userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + ownerId + " не найден "));
        if (itemId == null) {
            throw new ValidationException("При обновлении вещи не указан идентификатор вещи");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена!"));
        if (!Objects.equals(ownerId,item.getOwnerId())) {
            throw new ValidationException("Указанный пользователь не является владельцем его вещи. Редактировать вещь может только её владелец.");
        }
        if (itemDTO.getName() != null) {
            item.setName(itemDTO.getName());
        }
        if (itemDTO.getDescription() != null) {
            item.setDescription(itemDTO.getDescription());
        }
        if (itemDTO.getAvailable() != null) {
            item.setAvailable(itemDTO.getAvailable());
        }
        return ItemMap.itemToItemDTO(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void deleteItem(Long itemId) {
        if (itemId == null) {
            throw new ValidationException("При удалении вещи не указан идентификатор вещи");
        }
        itemRepository.deleteById(itemId);
    }

    @Override
    public ItemDTO getItem(Long itemId) {
        if (itemId == null) {
            throw new ValidationException("При поиске вещи не указан идентификатор вещи");
        }
        return ItemMap.itemToItemDTO(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена!")));
    }

    @Override
    public Collection<ItemDTO> getAllOwnerItems(Long ownerId) {
        if (ownerId == null) {
            throw new ValidationException("При поиске вещей пользователя не указан идентификатор владельца");
        }
        return itemRepository.findByOwnerId(ownerId).stream()
                .map(ItemMap::itemToItemDTO)
                .toList();
    }

    @Override
    public Collection<ItemDTO> findByNameDescription(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }
        return  itemRepository.findByNameDescription(text,text).stream()
                .map(ItemMap::itemToItemDTO)
                .toList();
    }

    @Override
    @Transactional
    public CommentsDTO addCommentToItem(Long itemId, CommentsDTO commentsDTO, Long authorId) {
        if (authorId == null) {
            throw new ValidationException("Идентификатор автора должен быть заполнен!");
        }
        User user = userRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Автор с id " + authorId + " не найден "));
        if (itemId == null) {
            throw new ValidationException("Идентификатор вещи должен быть заполнен!");
        }
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с id " + itemId + " не найдена"));
        if (!item.getAvailable()) {
            throw new ValidationException("Вещь не доступна для бронирования!");
        }
        //получаем список завершенных бронирований текущего пользователя
        List<Booking> bookings = bookingRepository.findAllByUserIdAndState(authorId, StatusEnum.APPROVED.name(),
                StateEnum.PAST.name(), LocalDateTime.now());
        log.trace("bookings.size={}",bookings.size());
        // если список пустой, то либо автор не бронил эту вещь, либо бронь не завершена
        if (bookings.isEmpty()) {
            throw new ValidationException("Отзыв может оставить только тот пользователь, " +
                    "который брал эту вещь в аренду, и только после окончания срока аренды!");
        }
        Comment comment = CommentMap.toComment(commentsDTO,item,user);
        CommentsDTO result = CommentMap.toCommentsDTO(commentRepository.save(comment));
        return result;
    }
}
