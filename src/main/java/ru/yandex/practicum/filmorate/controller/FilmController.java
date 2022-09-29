package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    @GetMapping
    public ResponseEntity<List<Film>> findAll() {
        List<Film> films = service.findAll();
        return ResponseEntity.ok().body(films);
    }

    @PostMapping
    @Validated({ValidationGroup.OnCreate.class})
    public ResponseEntity<Film> create(@Valid @RequestBody Film film) {
        Film filmCreated = service.create(film);
        return ResponseEntity.status(201).body(filmCreated);
    }

    @PutMapping
    @Validated({ValidationGroup.OnUpdate.class})
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        Film filmUpdated = service.update(film);
        return ResponseEntity.ok().body(filmUpdated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> findById(@PathVariable Long id) {
        Film film = service.findById(id);
        return ResponseEntity.ok().body(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> addLike(@PathVariable Long id, @PathVariable Long userId) {
        service.addLike(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        service.deleteLike(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> findPopular(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        List<Film> popularFilms = service.findPopular(count);
        return ResponseEntity.ok().body(popularFilms);
    }
}
