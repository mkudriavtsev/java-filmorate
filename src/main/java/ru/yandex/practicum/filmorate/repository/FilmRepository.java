package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    List<Film> findAll();

    Optional<Film> findById(Long id);

    Film save(Film film);

    Film update(Film film);

    void addLike(Film film, User user);

    void deleteLike(Film film, User user);
}
