package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.impl.UserInMemoryRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerMockMvcIntegrationTest {

    private static final URI uri = URI.create("/users");
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserInMemoryRepository repository;

    @AfterEach
    public void resetRepository() {
        repository.deleteAll();
        repository.resetIdCounter();
    }

    @Test
    public void givenUsers_whenGetUsers_thenStatus200() throws Exception {
        User user = getTestUser();
        repository.save(user);
        mockMvc.perform(
                        get(uri))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(user))));
    }

    @Test
    public void givenUser_whenCreate_thenStatus201() throws Exception {
        User user = getTestUser();
        user.setId(null);
        User expectedUser = getTestUser();
        mockMvc.perform(
                        post(uri)
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUser)));
    }

    @Test
    public void givenUserWithFailLogin_whenCreate_thenStatus400() throws Exception {
        User user = getTestUser();
        user.setId(null);
        user.setLogin("dolore ullamco");
        mockMvc.perform(
                        post(uri)
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenUserWithFailEmail_whenCreate_thenStatus400() throws Exception {
        User user = getTestUser();
        user.setId(null);
        user.setEmail("mail.ru");
        mockMvc.perform(
                        post(uri)
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenUserWithFailBirthday_whenCreate_thenStatus400() throws Exception {
        User user = getTestUser();
        user.setId(null);
        user.setBirthday(LocalDate.of(2466, 8, 20));
        mockMvc.perform(
                        post(uri)
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void givenUserWithNullName_whenCreate_thenStatus210AndAddsUserWithNameEqualsLogin() throws Exception {
        User user = getTestUser();
        user.setId(null);
        user.setName(null);
        User expectedUser = getTestUser();
        expectedUser.setName(expectedUser.getLogin());
        mockMvc.perform(
                        post(uri)
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedUser)));
    }

    @Test
    public void givenUser_whenUpdate_thenStatus200() throws Exception {
        User user = getTestUser();
        repository.save(user);
        User updatedUser = getUpdatedUser();
        mockMvc.perform(
                        put(uri)
                                .content(objectMapper.writeValueAsString(updatedUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)));
    }

    @Test
    public void givenUserWithFailId_whenUpdate_thenStatus404() throws Exception {
        User user = getTestUser();
        repository.save(user);
        User updatedUser = getUpdatedUser();
        updatedUser.setId(-1);
        mockMvc.perform(
                        put(uri)
                                .content(objectMapper.writeValueAsString(updatedUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }


    private User getTestUser() {
        User user = new User();
        user.setId(1);
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1946, 8,20));
        return user;
    }

    private User getUpdatedUser() {
        User user = new User();
        user.setId(1);
        user.setLogin("doloreUpdate");
        user.setName("est adipisicing");
        user.setEmail("mail@yandex.ru");
        user.setBirthday(LocalDate.of(1976, 9, 20));
        return user;
    }
}