package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.UserChecker;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@Slf4j
public class UserController {
    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public Collection<UserDto> findAll() {
        return userService.findAll();
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody User user) {
        UserChecker.emailChecker(user.getEmail());
        log.trace("Передали на добавление пользователя {}", user);
        return userService.create(user);
    }

    @PatchMapping("/{id}")
    public UserDto update(@PathVariable long id,
                          @Valid @RequestBody UpdateUserRequest request) {
        log.trace("Передали на обновление пользователя {} {}", id, request);
        return userService.update(id, request);
    }

    @GetMapping("/{id}")
    public UserDto findUserId(@PathVariable long id) {
        log.trace("Нужно вернуть пользователя с id {}", id);
        return userService.findUserId(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUserId(@PathVariable long id) {
        log.trace("Нужно удалить пользователя с id {}", id);
        userService.deleteUserId(id);
    }
}
