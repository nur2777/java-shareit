package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.service.ItemServiceImpl;

import java.util.Collection;

@RestController
@RequestMapping("/items")
public class ItemControllerImpl implements ItemController {

    public static final String SHARER_USER_ID = "X-Sharer-User-Id";
    private final ItemServiceImpl itemService;

    @Autowired
    public ItemControllerImpl(ItemServiceImpl itemService) {
        this.itemService = itemService;
    }

    @Override
    @PostMapping
    public ItemDTO add(@Valid @RequestBody ItemDTO newItem,
                       @RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemService.createItem(newItem,ownerId);
    }

    @Override
    @PatchMapping("/{itemId}")
    public ItemDTO update(@PathVariable Long itemId,
                          @RequestBody ItemDTO itemDTO,
                          @RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemService.updateItem(itemId,itemDTO,ownerId);
    }

    @Override
    @GetMapping("/{itemId}")
    public ItemDTO getItem(@Valid @PathVariable Long itemId) {
        return itemService.getItem(itemId);
    }

    @Override
    @GetMapping
    public Collection<ItemDTO> getAllOwnerItems(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemService.getAllOwnerItems(ownerId);
    }

    @Override
    @GetMapping("/search")
    public Collection<ItemDTO> findByNameDescription(
            @RequestParam String text) {
        return itemService.findByNameDescription(text);
    }
}
