package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.UserInMemoryRepository;
import ru.yandex.practicum.filmorate.service.GenericService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements GenericService<User> {

    private final UserInMemoryRepository repository;

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User create(User user) {
        List<User> users = repository.findAll();
        users.stream()
                .filter(u -> u.getLogin().equals(user.getLogin()))
                .findAny()
                .ifPresent(u -> {
                    throw new UserAlreadyExistException("Пользователь с таким логином уже зарегистрирован");
                });
        user.setId(repository.getNextID());
        if (Objects.isNull(user.getName())) {
            user.setName(user.getLogin());
        }
        log.info("Пользователь с id " + user.getId() + " зарегистрирован");
        return repository.save(user);
    }

    @Override
    public User update(User user) {
        Optional<User> userOptional = repository.findById(user.getId());
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + user.getId() + " не найден");
        }
        List<User> users = repository.findAll();
        users.stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .filter(u -> u.getLogin().equals(user.getLogin()))
                .findAny()
                .ifPresent(u -> {
                    throw new UserAlreadyExistException("Пользователь с таким логином уже зарегистрирован");
                });
        if (Objects.isNull(user.getName())) {
            user.setName(user.getLogin());
        }
        log.info("Данные пользователя с id " + user.getId() + " обновлены");
        return repository.save(user);
    }
}
