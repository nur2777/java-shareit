package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dao.ItemDAO;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.mapping.ItemMap;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDAO;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemDAO itemDAO;
    private final UserDAO userDAO;

    @Override
    public ItemDTO createItem(ItemDTO itemDTO, Long ownerId) {
        if (ownerId == null) {
            throw new ValidationException("При создании вещи не указан его владелец");
        }
        User user = userDAO.getUserById(ownerId);
        Item item = ItemMap.itemDTOToItem(itemDTO);
        item.setOwnerId(ownerId);
        return ItemMap.itemToItemDTO(itemDAO.createItem(item));
    }

    @Override
    public ItemDTO updateItem(Long itemId, ItemDTO itemDTO,  Long ownerId) {
        if (ownerId == null) {
            throw new ValidationException("При обновлении вещи не указан его владелец");
        }
        User user = userDAO.getUserById(ownerId);
        if (itemId == null) {
            throw new ValidationException("При обновлении вещи не указан идентификатор вещи");
        }
        Long itemOwnerId = itemDAO.getItemById(itemId).getOwnerId();
        if (!Objects.equals(ownerId,itemOwnerId)) {
            throw new ValidationException("Указанный пользователь не является владельцем его вещи. Редактировать вещь может только её владелец.");
        }
        Item item = ItemMap.itemDTOToItem(itemDTO);
        item.setId(itemId);
        return ItemMap.itemToItemDTO(itemDAO.updateItem(item));
    }

    @Override
    public void deleteItem(Long itemId) {
        if (itemId == null) {
            throw new ValidationException("При удалении вещи не указан идентификатор вещи");
        }
        itemDAO.deleteItem(itemId);
    }

    @Override
    public ItemDTO getItem(Long itemId) {
        if (itemId == null) {
            throw new ValidationException("При поиске вещи не указан идентификатор вещи");
        }
        return ItemMap.itemToItemDTO(itemDAO.getItemById(itemId));
    }

    @Override
    public Collection<ItemDTO> getAllOwnerItems(Long ownerId) {
        if (ownerId == null) {
            throw new ValidationException("При поиске вещей пользователя не указан идентификатор владельца");
        }
        return itemDAO.getAllOwnerItems(ownerId).stream()
                .map(ItemMap::itemToItemDTO)
                .toList();
    }

    @Override
    public Collection<ItemDTO> findByNameDescription(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }
        return  itemDAO.findItemsByNameAndDesc(text).stream()
                .map(ItemMap::itemToItemDTO)
                .toList();
    }
}
