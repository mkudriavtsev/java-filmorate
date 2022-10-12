package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Objects;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("film.id"));
        film.setName(rs.getString("film.name"));
        film.setDescription(rs.getString("film.description"));
        if (Objects.nonNull(rs.getDate("film.release_date"))) {
            film.setReleaseDate(rs.getDate("film.release_date").toLocalDate());
        }
        film.setDuration(Duration.ofMinutes(rs.getInt("film.duration")));
        film.setRate(rs.getInt("film.rate"));
        film.setMpa(new Mpa(rs.getLong("mpa.id"), rs.getString("mpa.name")));
        return film;
    }
}
