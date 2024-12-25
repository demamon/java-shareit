package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class UserChecker {
    public static void emailChecker(String email) {
        if (email == null || email.isBlank()) {
            log.error("Почта не может быть пустой");
            throw new ValidationException("почта не может быть пустой");
        }
    }
}
