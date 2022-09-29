package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    long getNextId();

    List<Film> findAll();

    Optional<Film> findById(Long id);

    Film save(Film t);

    void deleteAll();
}
