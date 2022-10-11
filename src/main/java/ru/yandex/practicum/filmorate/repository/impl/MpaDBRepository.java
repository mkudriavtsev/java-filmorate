package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaRepository;

import java.util.List;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class MpaDBRepository implements MpaRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query("SELECT * FROM mpa ORDER BY id", new MpaRowMapper());
    }

    @Override
    public Optional<Mpa> findById(Long id) {
        String sqlQuery = "SELECT * FROM mpa WHERE id = ?";
        List<Mpa> mpaList = jdbcTemplate.query(sqlQuery, new MpaRowMapper(), id);
        if (CollectionUtils.isEmpty(mpaList)) {
            return Optional.empty();
        } else {
            return Optional.of(mpaList.get(0));
        }
    }
}
