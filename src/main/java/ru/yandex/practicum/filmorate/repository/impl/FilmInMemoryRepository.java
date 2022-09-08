package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.GenericRepository;

import java.util.*;

@Component
public class FilmInMemoryRepository implements GenericRepository<Film, Integer> {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentID;

    public int getNextID() {
        return ++currentID;
    }

    public void resetIdCounter() {
        currentID = 0;
    }

    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> findById(Integer id) {
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
