package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import javax.validation.Valid;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = service.findAll();
        return ResponseEntity.ok().body(users);
    }

    @PostMapping
    @Validated({ValidationGroup.OnCreate.class})
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User u = service.create(user);
        return ResponseEntity.status(201).body(u);
    }

    @PutMapping
    @Validated({ValidationGroup.OnUpdate.class})
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        User u = service.update(user);
        return ResponseEntity.ok().body(u);
    }
}
