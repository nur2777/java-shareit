package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item,Long> {

    List<Item> findByOwnerId(long ownerId);

    List<Item> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(String name, String description);
}
