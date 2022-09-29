package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.util.*;

@Component
public class FilmInMemoryRepository implements FilmRepository {
    private final Map<Long, Film> films = new HashMap<>();
    private long currentID;

    @Override
    public long getNextId() {
        return ++currentID;
    }

    public void resetIdCounter() {
        currentID = 0;
    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(Long id) {
        return films.containsKey(id) ? Optional.of(films.get(id)) : Optional.empty();
    }

    @Override
    public Film save(Film film) {
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteAll() {
        films.clear();
    }

}
