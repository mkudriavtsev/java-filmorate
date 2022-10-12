package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService {

    private final MpaRepository repository;

    public List<Mpa> findAll() {
        return repository.findAll();
    }

    public Mpa findById(Long id) {
        Optional<Mpa> mpa = repository.findById(id);
        return mpa.orElseThrow(() -> new NotFoundException("Рейтинг MPA с id " + id + " не найден"));
    }
}
