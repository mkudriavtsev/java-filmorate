package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.impl.FilmInMemoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {

    private final FilmInMemoryRepository repository;

    public List<Film> findAll() {
        return repository.findAll();
    }

    public Film create(Film film) {
        film.setId(repository.getNextID());
        log.info("Фильм с id " + film.getId() + " создан");
        return repository.save(film);
    }

    public Film update(Film film) {
        Optional<Film> filmOptional = repository.findById(film.getId());
        if (filmOptional.isEmpty()) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        log.info("Фильм с id " + film.getId() + " обновлен");
        return repository.save(film);
    }
}
