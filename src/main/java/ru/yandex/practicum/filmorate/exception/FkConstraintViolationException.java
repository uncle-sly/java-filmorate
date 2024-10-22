package ru.yandex.practicum.filmorate.exception;

public class FkConstraintViolationException extends RuntimeException {
    public FkConstraintViolationException(String message) {
        super(message);
    }
}

