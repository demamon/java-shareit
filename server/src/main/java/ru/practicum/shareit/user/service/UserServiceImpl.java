package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserStorage;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
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
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        user = userStorage.save(user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(long id, UpdateUserRequest request) {
        Optional<User> mayBeUser = userStorage.findById(id);
        if (mayBeUser.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        User updatedUser = mayBeUser.get();
        UserMapper.updateUserFields(updatedUser, request);
        updatedUser = userStorage.save(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto findUserId(long id) {
        Optional<User> mayBeUser = userStorage.findById(id);
        if (mayBeUser.isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        return UserMapper.mapToUserDto(mayBeUser.get());
    }

    @Override
    public void deleteUserId(long id) {
        userStorage.deleteById(id);
    }
}
