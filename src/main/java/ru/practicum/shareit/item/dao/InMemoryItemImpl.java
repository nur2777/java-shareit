package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

/**
 * Класс реализует хранение данных о вещах в памяти приложения
 */
@Component("InMemoryItemImpl")
@Slf4j
public class InMemoryItemImpl implements ItemDAO {

    private final Map<Long, Item> items = new HashMap<>();

    /** Получаем следующий уникальный идентификатор
     * @return новый идентификатор вещи
     */
    private long getNextItemId() {
        long currItemId = items.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        currItemId++;
        return currItemId;
    }

    @Override
    public Item createItem(Item newItem) {
        newItem.setId(getNextItemId());
        items.put(newItem.getId(),newItem);
        log.info("Вещь {} успешно добавлена c идентификатором {}.", newItem.getName(),newItem.getId());
        return newItem;
    }

    @Override
    public Item updateItem(Item updatedItem) {
        if (items.containsKey(updatedItem.getId())) {
            Item item = items.get(updatedItem.getId());
            if (updatedItem.getName() != null && !updatedItem.getName().isEmpty()) {
                item.setName(updatedItem.getName());
            }
            if (updatedItem.getDescription() != null && !updatedItem.getDescription().isEmpty()) {
                item.setDescription(updatedItem.getDescription());
            }
            if (updatedItem.getAvailable() != null) {
                item.setAvailable(updatedItem.getAvailable());
            }
            return item;
        } else {
            throw new NotFoundException("Не найдена вещь с идентификатором " + updatedItem.getId());
        }
    }

    @Override
    public void deleteItem(Long itemId) {
        if (items.containsKey(itemId)) {
            items.remove(itemId);
            log.info("Вещь c идентификатором {} успешно удалена.", itemId);
        } else {
            throw new NotFoundException("Не найдена вещь с идентификатором " + itemId);
        }

    }

    @Override
    public Item getItemById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public Collection<Item> getAllOwnerItems(Long ownerId) {
        List<Item> result = items.values().stream()
                .filter(item -> Objects.equals(item.getOwnerId(), ownerId))
                .toList();
        return result;
    }

    @Override
    public Collection<Item> findItemsByNameAndDesc(String text) {
        List<Item> result = items.values().stream()
                .filter(Item::getAvailable)
                .filter(item -> (item.getName().toUpperCase().contains(text.toUpperCase())
                        || item.getDescription().toUpperCase().contains(text.toUpperCase())))
                .toList();
        log.info("Result length = {}",result.size());
        return result;
    }
}
