package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.GenreRepository;
import ru.yandex.practicum.filmorate.repository.MpaRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;

    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    public Film create(Film film) {
        loadMpa(film);
        loadGenres(film);
        filmRepository.save(film);
        log.info("Фильм с id " + film.getId() + " создан");
        return film;
    }

    public Film update(Film film) {
        Optional<Film> filmOptional = filmRepository.findById(film.getId());
        if (filmOptional.isEmpty()) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        loadMpa(film);
        loadGenres(film);
        filmRepository.update(film);
        log.info("Фильм с id " + film.getId() + " обновлен");
        return film;
    }

    public Film findById(Long id) {
        Optional<Film> film = filmRepository.findById(id);
        return film.orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
    }

    public void addLike(Long id, Long userId) {
        Film film = filmRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        filmRepository.addLike(film, user);
        log.info("Пользователь с id " + userId + " поставил лайк фильму с id " + id);
    }

    public void deleteLike(Long id, Long userId) {
        Film film = filmRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        filmRepository.deleteLike(film, user);
        log.info("Пользователь с id " + userId + " удалил лайк у фильма с id " + id);
    }

    public List<Film> findPopular(Integer count) {
        List<Film> popularFilm =  filmRepository
                .findAll()
                .stream()
                .sorted(Comparator.comparing(film -> film.getLikes().size()))
                .collect(Collectors.toList());
        Collections.reverse(popularFilm);
        return popularFilm
                .stream()
                .limit(count)
                .collect(Collectors.toList());
    }

    private void loadMpa(Film film) {
        Optional<Mpa> mpaOptional = mpaRepository.findById(film.getMpa().getId());
        if (mpaOptional.isEmpty()) {
            throw new NotFoundException("Рейтинг MPA с id " + film.getMpa().getId() + " не найден");
        }
        film.setMpa(mpaOptional.get());
    }

    private void loadGenres(Film film) {
        Set<Genre> genreSet = new TreeSet<>(film.getGenres());
        film.getGenres().clear();
        for (Genre genre: genreSet) {
            Optional<Genre> genreOptional = genreRepository.findById(genre.getId());
            if (genreOptional.isEmpty()) {
                throw new NotFoundException("Жанр с id " + genre.getId() + " не найден");
            }
            film.getGenres().add(genreOptional.get());
        }
    }
}
