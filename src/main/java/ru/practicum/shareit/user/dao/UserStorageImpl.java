package ru.practicum.shareit.user.dao;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@Slf4j
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        log.debug("Возвращаем всех пользователей {}", users.values());
        return users.values();
    }

    @Override
    public User create(User user) {
        checkDuplicateEmail(user);
        user.setId(getNextId());
        log.debug("добавляем нового пользователя {}", user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        checkDuplicateEmail(newUser);
        log.debug("обновляем пользователя {}", newUser);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User findUserId(long id) {
        User user = users.get(id);
        if (user == null) {
            log.error("Пользователь с id = {} не найден", id);
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        log.debug("Возвращаем запрашиваемого пользователя {}, с id {}", user, id);
        return user;
    }

    @Override
    public void deleteUserId(long id) {
        users.remove(id);
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void checkDuplicateEmail(User user) {
        String email = user.getEmail();
        Long id = user.getId();
        Optional<User> maybeDuplicate = users.values().stream()
                .filter(user1 -> user1.getEmail().equals(email) && !Objects.equals(user1.getId(), id))
                .findFirst();
        if (maybeDuplicate.isPresent()) {
            log.error("Почта {} уже используется", user.getEmail());
            throw new ValidationException("Почта " + user.getEmail() + " уже используется");
        }
    }
}
