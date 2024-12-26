package ru.practicum.shareit.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Collection<UserDto> findAll() {
        return userStorage.findAll().stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        user = userStorage.create(user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto update(long id, UpdateUserRequest request) {
        User updatedUser = userStorage.findUserId(id);
        UserMapper.updateUserFields(updatedUser, request);
        updatedUser = userStorage.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto findUserId(long id) {
        User user = userStorage.findUserId(id);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public void deleteUserId(long id) {
        userStorage.deleteUserId(id);
    }
}
