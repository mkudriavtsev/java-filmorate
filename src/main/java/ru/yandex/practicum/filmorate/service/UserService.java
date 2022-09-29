package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.IllegalRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public User create(User user) {
        List<User> users = repository.findAll();
        users.stream()
                .filter(u -> Objects.equals(u.getLogin(), user.getLogin()))
                .findAny()
                .ifPresent(u -> {
                    throw new UserAlreadyExistException("Пользователь с таким логином уже зарегистрирован");
                });
        user.setId(repository.getNextID());
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        log.info("Пользователь с id " + user.getId() + " зарегистрирован");
        return repository.save(user);
    }

    public User update(User user) {
        Optional<User> userOptional = repository.findById(user.getId());
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        List<User> users = repository.findAll();
        users.stream()
                .filter(u -> !Objects.equals(u.getId(), user.getId()))
                .filter(u -> Objects.equals(u.getLogin(), user.getLogin()))
                .findAny()
                .ifPresent(u -> {
                    throw new UserAlreadyExistException("Пользователь с таким логином уже зарегистрирован");
                });
        if (StringUtils.isBlank(user.getName())) {
            user.setName(user.getLogin());
        }
        log.info("Данные пользователя с id " + user.getId() + " обновлены");
        return repository.save(user);
    }

    public User findById(Long id) {
        Optional<User> user = repository.findById(id);
        return user.orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
    }

    public void addFriend(Long id, Long friendId) {
        Map<Long, User> users = findTwoUsersById(id, friendId);
        users.get(id).getFriends().add(friendId);
        users.get(friendId).getFriends().add(id);
    }

    public void deleteFriend(Long id, Long friendId) {
        Map<Long, User> users = findTwoUsersById(id, friendId);
        users.get(id).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(id);
    }

    public Map<Long, User> findTwoUsersById(Long firstId, Long secondId) {
        if (firstId.equals(secondId)) {
            throw new IllegalRequestException("Id пользователей не должны совпадать");
        }
        User firstUser = repository
                .findById(firstId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + firstId + " не найден"));
        User secondUser = repository
                .findById(secondId)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + secondId + " не найден"));
        Map<Long, User> users = new HashMap<>();
        users.put(firstId, firstUser);
        users.put(secondId, secondUser);
        return users;
    }

    public List<User> findFriendsById(Long id) {
        User user = repository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден"));
        return user
                .getFriends()
                .stream()
                .map(this::findById)
                .collect(Collectors.toList());
    }

    public List<User> findCommonFriends(Long id, Long otherId) {
        Map<Long, User> users = findTwoUsersById(id, otherId);
        Set<Long> userFriends = users.get(id).getFriends();
        Set<Long> otherFriends = users.get(otherId).getFriends();
        return userFriends
                .stream()
                .filter(otherFriends::contains)
                .map(this::findById)
                .collect(Collectors.toList());
    }
}
