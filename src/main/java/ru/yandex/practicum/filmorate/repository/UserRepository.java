package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    long getNextID();

    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);

    void deleteAll();
}
