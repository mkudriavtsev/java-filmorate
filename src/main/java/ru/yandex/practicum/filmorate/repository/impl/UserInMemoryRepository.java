package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.GenericRepository;

import java.util.*;

@Component
public class UserInMemoryRepository implements GenericRepository<User, Integer> {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentID;

    public int getNextID() {
        return ++currentID;
    }

    public void resetIdCounter() {
        currentID = 0;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> findById(Integer id) {
        return users.containsKey(id) ? Optional.of(users.get(id)) : Optional.empty();
    }

    @Override
    public User save(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

}
