package ru.yandex.practicum.filmorate.repository.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Primary
@RequiredArgsConstructor
public class FilmDBRepository implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> findAll() {
        String sqlQuery = "SELECT * FROM film JOIN mpa ON film.mpa_id = mpa.id";
        List<Film> films = jdbcTemplate.query(sqlQuery, new FilmRowMapper());
        films.forEach(this::loadLikes);
        films.forEach(this::loadGenres);
        return films;
    }

    @Override
    public Optional<Film> findById(Long id) {
        String sqlQuery = "SELECT * FROM film JOIN mpa ON film.mpa_id = mpa.id WHERE film.id = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, new FilmRowMapper(), id);
        if (CollectionUtils.isEmpty(films)) {
            return Optional.empty();
        } else {
            loadLikes(films.get(0));
            loadGenres(films.get(0));
            return Optional.of(films.get(0));
        }
    }

    @Override
    public Film save(Film film) {
        String sqlQuery = "INSERT INTO film(name, description, release_date, duration, rate, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration().toMinutes());
            stmt.setInt(5, film.getRate());
            stmt.setLong(6, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        String sqlQueryGenres = "INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?)";
        film.getGenres().forEach(genre -> jdbcTemplate.update(sqlQueryGenres, film.getId(), genre.getId()));
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE film SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, rate = ?, mpa_id = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());
        String sqlQueryDeleteGenres = "DELETE FROM film_genre " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQueryDeleteGenres, film.getId());
        String sqlQueryInsertGenres = "INSERT INTO film_genre(film_id, genre_id) VALUES (?, ?)";
        film.getGenres().forEach(genre -> jdbcTemplate.update(sqlQueryInsertGenres, film.getId(), genre.getId()));
        loadLikes(film);
        return film;
    }

    @Override
    public void addLike(Film film, User user) {
        String sqlQuery = "INSERT INTO likes(film_id, user_id) " +
                "VALUES(?, ?)";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    @Override
    public void deleteLike(Film film, User user) {
        String sqlQuery = "DELETE FROM likes " +
                "WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId(), user.getId());
    }

    private void loadLikes(Film film) {
        String sqlQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        List<Long> userIds = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> rs.getLong("user_id"), film.getId());
        userIds.forEach(id -> film.getLikes().add(id));
    }

    private void loadGenres(Film film) {
        String sqlQuery = "SELECT genre.id, genre.name FROM genre " +
                "JOIN film_genre ON genre.id = film_genre.genre_id " +
                "WHERE film_genre.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sqlQuery, new GenreRowMapper(), film.getId());
        genres.forEach(genre -> film.getGenres().add(genre));
    }
}
