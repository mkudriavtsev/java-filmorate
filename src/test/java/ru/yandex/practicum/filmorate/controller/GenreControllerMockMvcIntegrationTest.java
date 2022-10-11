package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Genre;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class GenreControllerMockMvcIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenGenres_whenGetGenres_thenStatus200() throws Exception {
        mockMvc.perform(
                        get(URI.create("/genres")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getGenreList())));
    }

    @Test
    public void givenGenre_whenGetById_thenStatus200() throws Exception {
        mockMvc.perform(
                        get(URI.create("/genres/1")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getGenreList().get(0))));
    }

    @Test
    public void givenGenreWithFailId_whenGetById_thenStatus404() throws Exception {
        mockMvc.perform(
                        get(URI.create("/genres/-1")))
                .andExpect(status().isNotFound());
    }

    private List<Genre> getGenreList() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(1L, "Комедия"));
        genres.add(new Genre(2L, "Драма"));
        genres.add(new Genre(3L, "Мультфильм"));
        genres.add(new Genre(4L, "Триллер"));
        genres.add(new Genre(5L, "Документальный"));
        genres.add(new Genre(6L, "Боевик"));
        return genres;
    }
}