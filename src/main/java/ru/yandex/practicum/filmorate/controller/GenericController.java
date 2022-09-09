package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.service.GenericService;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import javax.validation.Valid;
import java.util.List;
@RequiredArgsConstructor
public abstract class GenericController<T> {

    private final GenericService<T> service;

    @GetMapping
    public ResponseEntity<List<T>> findAll() {
        List<T> entities = service.findAll();
        return ResponseEntity.ok().body(entities);
    }

    @PostMapping
    @Validated({ValidationGroup.OnCreate.class})
    public ResponseEntity<T> create(@Valid @RequestBody T entity) {
        T entityCreated = service.create(entity);
        return ResponseEntity.status(201).body(entityCreated);
    }

    @PutMapping
    @Validated({ValidationGroup.OnUpdate.class})
    public ResponseEntity<T> update(@Valid @RequestBody T entity) {
        T entityUpdated = service.update(entity);
        return ResponseEntity.ok().body(entityUpdated);
    }
}
