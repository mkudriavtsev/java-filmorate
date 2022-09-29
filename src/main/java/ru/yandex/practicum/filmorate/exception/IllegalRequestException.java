package ru.yandex.practicum.filmorate.exception;

public class IllegalRequestException extends RuntimeException {
    public IllegalRequestException(String message) {
        super(message);
    }
}
