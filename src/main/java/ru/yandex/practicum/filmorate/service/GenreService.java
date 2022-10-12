package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository repository;

    public List<Genre> findAll() {
        return repository.findAll();
    }

    public Genre findById(Long id) {
        Optional<Genre> genre = repository.findById(id);
        return genre.orElseThrow(() -> new NotFoundException("Жанр с id " + id + " не найден"));
    }
}
