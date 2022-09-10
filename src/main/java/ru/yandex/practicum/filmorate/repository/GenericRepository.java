package ru.yandex.practicum.filmorate.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T t);
    void deleteAll();
}
