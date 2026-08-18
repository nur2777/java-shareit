package ru.practicum.shareit.item.mapping;

import ru.practicum.shareit.item.dto.ItemDTO;
import ru.practicum.shareit.item.model.Item;

public class ItemMap {

    public static Item itemDTOToItem(ItemDTO itemDTO) {
        return Item.builder()
                .name(itemDTO.getName())
                .description(itemDTO.getDescription())
                .available(itemDTO.getAvailable())
                .build();
    }

    public static ItemDTO itemToItemDTO(Item item) {
        return ItemDTO.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }
}
