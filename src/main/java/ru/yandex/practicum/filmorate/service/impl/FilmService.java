package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.impl.FilmInMemoryRepository;
import ru.yandex.practicum.filmorate.service.GenericService;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService implements GenericService<Film> {

    private final FilmInMemoryRepository repository;

    @Override
    public List<Film> findAll() {
        return repository.findAll();
    }

    @Override
    public Film create(Film film) {
        film.setId(repository.getNextID());
        log.info("Фильм с id " + film.getId() + " создан");
        return repository.save(film);
    }

    @Override
    public Film update(Film film) {
        Optional<Film> filmOptional = repository.findById(film.getId());
        if (filmOptional.isEmpty()) {
            throw new NotFoundException("Фильм с id " + film.getId() + " не найден");
        }
        log.info("Фильм с id " + film.getId() + " обновлен");
        return repository.save(film);
    }
}
