package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import static ru.practicum.shareit.constants.Constants.SHARER_USER_ID;

@RestController
@Slf4j
@RequestMapping(path = "/requests")
public class ItemRequestControllerImpl implements ItemRequestController {

    @Override
    @PostMapping
    public ItemRequestDto addNewRequest(@Valid @RequestBody ItemRequestDto newRequest,
                                        @RequestHeader(SHARER_USER_ID) Long authorId) {
        return null;
    }
}
