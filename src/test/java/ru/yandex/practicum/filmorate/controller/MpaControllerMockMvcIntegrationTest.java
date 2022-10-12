package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class MpaControllerMockMvcIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenMpaList_whenGetMpaList_thenStatus200() throws Exception {
        mockMvc.perform(
                        get(URI.create("/mpa")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getMpaList())));
    }

    @Test
    public void givenMpa_whenGetById_thenStatus200() throws Exception {
        mockMvc.perform(
                        get(URI.create("/mpa/1")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(getMpaList().get(0))));
    }

    @Test
    public void givenMpaWithFailId_whenGetById_thenStatus404() throws Exception {
        mockMvc.perform(
                        get(URI.create("/mpa/-1")))
                .andExpect(status().isNotFound());
    }

    private List<Mpa> getMpaList() {
        List<Mpa> mpaList = new ArrayList<>();
        mpaList.add(new Mpa(1L, "G"));
        mpaList.add(new Mpa(2L, "PG"));
        mpaList.add(new Mpa(3L, "PG-13"));
        mpaList.add(new Mpa(4L, "R"));
        mpaList.add(new Mpa(5L, "NC-17"));
        return mpaList;
    }
}