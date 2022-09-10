package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.repository.impl.FilmInMemoryRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerMockMvcIntegrationTest {

    private static final URI uri = URI.create("/films");
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmInMemoryRepository repository;

    @AfterEach
    public void resetRepository() {
        repository.deleteAll();
        repository.resetIdCounter();
    }

    @Test
    public void givenFilms_whenGetFilms_thenStatus200() throws Exception {
        Film film = getTestFilm();
        repository.save(film);
        mockMvc.perform(
                        get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(film))));
    }

    @Test
    public void givenFilm_whenCreate_thenStatus201() throws Exception {
        Film film = getTestFilm();
        film.setId(null);
        Film expectedFilm = getTestFilm();
        mockMvc.perform(
                        post(uri)
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedFilm)));
    }

    @Test
    public void givenFilmWithFailName_whenCreate_thenStatus400() throws Exception {
        Film film = getTestFilm();
        film.setId(null);
        film.setName("");
        mockMvc.perform(
                        post(uri)
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenFilmWithFailDescription_whenCreate_thenStatus400() throws Exception {
        Film film = getTestFilm();
        film.setId(null);
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят " +
                "разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                "который за время «своего отсутствия», стал кандидатом Коломбани.");
        mockMvc.perform(
                        post(uri)
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenFilmWithFailReleaseDate_whenCreate_thenStatus400() throws Exception {
        Film film = getTestFilm();
        film.setId(null);
        film.setReleaseDate(LocalDate.of(1890, 3, 25));
        mockMvc.perform(
                        post(uri)
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenFilmWithFailDuration_whenCreate_thenStatus400() throws Exception {
        Film film = getTestFilm();
        film.setId(null);
        film.setDuration(Duration.ofMinutes(-200));
        mockMvc.perform(
                        post(uri)
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenFilm_whenUpdate_thenStatus200() throws Exception {
        Film film = getTestFilm();
        repository.save(film);
        Film updatedFilm = getUpdatedTestFilm();
        mockMvc.perform(
                        put(uri)
                                .content(objectMapper.writeValueAsString(updatedFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedFilm)));
    }

    @Test
    public void givenFilmWithFailId_whenUpdate_thenStatus404() throws Exception {
        Film film = getTestFilm();
        repository.save(film);
        Film updatedFilm = getUpdatedTestFilm();
        updatedFilm.setId(-1L);
        mockMvc.perform(
                        put(uri)
                                .content(objectMapper.writeValueAsString(updatedFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    private Film getTestFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setDuration(Duration.ofMinutes(100));
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        return film;
    }

    private Film getUpdatedTestFilm() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Film Updated");
        film.setDescription("New film update description");
        film.setDuration(Duration.ofMinutes(190));
        film.setReleaseDate(LocalDate.of(1989,4,17));
        return film;
    }


}