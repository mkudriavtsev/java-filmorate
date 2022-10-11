package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.FilmDBRepository;
import ru.yandex.practicum.filmorate.repository.impl.UserDBRepository;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmControllerMockMvcIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FilmDBRepository filmRepository;
    @Autowired
    private UserDBRepository userRepository;

    @Test
    public void givenFilms_whenGetFilms_thenStatus200() throws Exception {
        Film film = filmRepository.save(getTestFilm());
        mockMvc.perform(
                        get(URI.create("/films")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(film))));
    }

    @Test
    public void givenFilm_whenCreate_thenStatus201() throws Exception {
        Film film = getTestFilm();
        Film expectedFilm = getTestFilm();
        expectedFilm.setId(1L);
        mockMvc.perform(
                        post(URI.create("/films"))
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedFilm)));
    }

    @Test
    public void givenFilmWithFailName_whenCreate_thenStatus400() throws Exception {
        Film film = getTestFilm();
        film.setName("");
        mockMvc.perform(
                        post(URI.create("/films"))
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenFilmWithFailDescription_whenCreate_thenStatus400() throws Exception {
        Film film = getTestFilm();
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят " +
                "разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                "который за время «своего отсутствия», стал кандидатом Коломбани.");
        mockMvc.perform(
                        post(URI.create("/films"))
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenFilmWithFailReleaseDate_whenCreate_thenStatus400() throws Exception {
        Film film = getTestFilm();
        film.setReleaseDate(LocalDate.of(1890, 3, 25));
        mockMvc.perform(
                        post(URI.create("/films"))
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenFilmWithFailDuration_whenCreate_thenStatus400() throws Exception {
        Film film = getTestFilm();
        film.setDuration(Duration.ofMinutes(-200));
        mockMvc.perform(
                        post(URI.create("/films"))
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenFilm_whenUpdate_thenStatus200() throws Exception {
        Film film = filmRepository.save(getTestFilm());
        Film updatedFilm = getUpdatedTestFilm();
        updatedFilm.setId(film.getId());
        mockMvc.perform(
                        put(URI.create("/films"))
                                .content(objectMapper.writeValueAsString(updatedFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedFilm)));
    }

    @Test
    public void givenFilmWithFailId_whenUpdate_thenStatus404() throws Exception {
        Film updatedFilm = getUpdatedTestFilm();
        updatedFilm.setId(-1L);
        mockMvc.perform(
                        put(URI.create("/films"))
                                .content(objectMapper.writeValueAsString(updatedFilm))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenFilm_whenGetById_thenStatus200() throws Exception {
        Film film = filmRepository.save(getTestFilm());
        mockMvc.perform(
                        get(URI.create("/films/" + film.getId())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film)));
    }

    @Test
    public void givenFilmWithFailId_whenGetById_ThenStatus404() throws Exception {
        mockMvc.perform(
                        get(URI.create("/films/-1")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenFilmAndUser_whenAddLike_ThenStatus204AndAddedLike() throws Exception {
        Film film = filmRepository.save(getTestFilm());
        User user = userRepository.save(getTestUser());
        mockMvc.perform(
                        put(URI.create("/films/" + film.getId() + "/like/" + user.getId())))
                .andExpect(status().isNoContent());
        Film filmFromDB = filmRepository.findById(film.getId()).orElseThrow();
        assertTrue(filmFromDB.getLikes().contains(user.getId()));
    }

    @Test
    public void givenFilmAndUser_whenDeleteLike_ThenStatus204AndDeletedLike() throws Exception {
        Film film = filmRepository.save(getTestFilm());
        User user = userRepository.save(getTestUser());
        filmRepository.addLike(film, user);
        mockMvc.perform(
                        delete(URI.create("/films/" + film.getId() + "/like/" + user.getId())))
                .andExpect(status().isNoContent());
        Film filmFromDB = filmRepository.findById(film.getId()).orElseThrow();
        assertFalse(filmFromDB.getLikes().contains(user.getId()));
    }

    @Test
    public void givenFilmWithLike_whenGetPopularWithCount_thenStatus200() throws Exception {
        Film film = filmRepository.save(getTestFilm());
        User user = userRepository.save(getTestUser());
        filmRepository.addLike(film, user);
        Film filmFromDB = filmRepository.findById(film.getId()).orElseThrow();
        mockMvc.perform(
                        get(URI.create("/films/popular?count=1")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(filmFromDB))));
    }

    @Test
    public void givenFilmWithLike_whenGetPopularWithoutCount_thenStatus200() throws Exception {
        Film film = filmRepository.save(getTestFilm());
        User user = userRepository.save(getTestUser());
        filmRepository.addLike(film, user);
        Film filmFromDB = filmRepository.findById(film.getId()).orElseThrow();
        mockMvc.perform(
                        get(URI.create("/films/popular")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(filmFromDB))));
    }

    private Film getTestFilm() {
        Film film = new Film();
        film.setName("nisi eiusmod");
        film.setDescription("adipisicing");
        film.setDuration(Duration.ofMinutes(100));
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setRate(1);
        film.setMpa(new Mpa(1L, "G"));
        return film;
    }

    private Film getUpdatedTestFilm() {
        Film film = new Film();
        film.setName("Film Updated");
        film.setDescription("New film update description");
        film.setDuration(Duration.ofMinutes(190));
        film.setReleaseDate(LocalDate.of(1989,4,17));
        film.setRate(2);
        film.setMpa(new Mpa(1L, "G"));
        return film;
    }

    private User getTestUser() {
        User user = new User();
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1946, 8,20));
        return user;
    }


}