package ru.yandex.practicum.filmorate.repository.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;

@Component
public class UserInMemoryRepository implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private long currentID;

    private long getNextID() {
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
    public Optional<User> findById(Long id) {
        return users.containsKey(id) ? Optional.of(users.get(id)) : Optional.empty();
    }

    @Override
    public User save(User user) {
        user.setId(getNextID());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    public void deleteAll() {
        users.clear();
    }

    @Override
    public void addFriend(User user, User friend) {
        users.get(user.getId()).getFriends().add(friend.getId());
    }

    @Override
    public void deleteFriend(User user, User friend) {
        users.get(user.getId()).getFriends().remove(friend.getId());
    }
}
