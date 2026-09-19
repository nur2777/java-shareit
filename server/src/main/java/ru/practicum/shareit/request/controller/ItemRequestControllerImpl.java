package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

import static ru.practicum.shareit.constants.Constants.SHARER_USER_ID;

@RestController
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestControllerImpl implements ItemRequestController {

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestControllerImpl(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @Override
    @PostMapping
    public ItemRequestDto addNewRequest(@Valid @RequestBody ItemRequestDto newRequest,
                                        @RequestHeader(SHARER_USER_ID) Long authorId) {
        return itemRequestService.addNewRequest(newRequest,authorId);
    }

    @Override
    @GetMapping
    public List<ItemRequestDto> getItemRequests(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemRequestService.getItemRequests(ownerId, false);
    }

    @Override
    @GetMapping("/all")
    public List<ItemRequestDto> getAllItemRequests(@RequestHeader(SHARER_USER_ID) Long ownerId) {
        return itemRequestService.getItemRequests(ownerId,true);
    }

    @Override
    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader(SHARER_USER_ID) Long authorId,
                                             @PathVariable Long requestId) {
        return itemRequestService.getItemRequestById(requestId);
    }
}
