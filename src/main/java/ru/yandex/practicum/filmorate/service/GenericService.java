package ru.yandex.practicum.filmorate.service;

import java.util.List;

public interface GenericService<T> {
    List<T> findAll();
    T create(T t);
    T update(T t);
}
