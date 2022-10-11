package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService service;

    @GetMapping
    public ResponseEntity<List<Mpa>> findAll() {
        List<Mpa> mpaList = service.findAll();
        return ResponseEntity.ok().body(mpaList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> findById(@PathVariable Long id) {
        Mpa mpa = service.findById(id);
        return ResponseEntity.ok().body(mpa);
    }
}
