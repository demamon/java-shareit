package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    Collection<UserDto> findAll();

    UserDto create(User user);

    UserDto update(long id, UpdateUserRequest request);

    UserDto findUserId(long id);

    void deleteUserId(long id);
}
