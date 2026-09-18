package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.ShareItUtils;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService{
    private final ItemRequestRepository itemReqRep;
    @Override
    @Transactional
    public ItemRequestDto addNewRequest(ItemRequestDto itemRequestDto, Long userId) {
        ShareItUtils.idIsNullCheck(userId,"Идентификатор пользователя, автора запроса,");
        return null;
    }
}
