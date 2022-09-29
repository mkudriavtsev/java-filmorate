package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
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
        User userCreated = service.create(user);
        return ResponseEntity.status(201).body(userCreated);
    }

    @PutMapping
    @Validated({ValidationGroup.OnUpdate.class})
    public ResponseEntity<User> update(@Valid @RequestBody User user) {
        User userUpdated = service.update(user);
        return ResponseEntity.ok().body(userUpdated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        User user = service.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.addFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<Void> deleteFriend(@PathVariable Long id, @PathVariable Long friendId) {
        service.deleteFriend(id, friendId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> findFriendsById(@PathVariable Long id) {
        List<User> friends = service.findFriendsById(id);
        return ResponseEntity.ok().body(friends);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> findCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        List<User> commonFriends = service.findCommonFriends(id, otherId);
        return ResponseEntity.ok().body(commonFriends);
    }
}
