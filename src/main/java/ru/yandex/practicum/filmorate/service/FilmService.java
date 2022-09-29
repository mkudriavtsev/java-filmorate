package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;

    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    public Film create(Film film) {
        film.setId(filmRepository.getNextId());
        log.info("Фильм с id " + film.getId() + " создан");
        return filmRepository.save(film);
    }

    public Film update(Film film) {
        Optional<Film> filmOptional = filmRepository.findById(film.getId());
        if (filmOptional.isEmpty()) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        log.info("Фильм с id " + film.getId() + " обновлен");
        return filmRepository.save(film);
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
        film.getLikes().add(user.getId());
    }

    public void deleteLike(Long id, Long userId) {
        Film film = filmRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id " + id + " не найден"));
        User user = userRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + userId + " не найден"));
        film.getLikes().remove(user.getId());
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

}
