package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import javax.validation.Valid;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
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
        Film f = service.create(film);
        return ResponseEntity.status(201).body(f);
    }

    @PutMapping
    @Validated({ValidationGroup.OnUpdate.class})
    public ResponseEntity<Film> update(@Valid @RequestBody Film film) {
        Film f = service.update(film);
        return ResponseEntity.ok().body(f);
    }
}
