package ru.practicum.shareit.request.mapping;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestItemDTO;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ItemRequestMap {

    public static ItemRequest itemRequestDtoToItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        if (itemRequestDto.getDescription() != null) {
            itemRequest.setDescription(itemRequestDto.getDescription());
        }
        return itemRequest;
    }

    public static ItemRequestDto itemRequestToItemRequestDTO(ItemRequest itemRequest, List<Item> items) {
        List<RequestItemDTO> requestItemDTOS = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            requestItemDTOS = items.stream()
                                    .filter(item -> item.getRequestId().equals(itemRequest.getId()))
                                    .map(item -> RequestItemDTO.builder()
                                                    .id(item.getId())
                                                    .name(item.getName())
                                                    .ownerId(item.getOwnerId())
                                                    .build())
                                    .toList();
        }


        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreationDate())
                .items(requestItemDTOS)
                .build();
    }
}
