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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerMockMvcIntegrationTest {
    
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
                        get(URI.create("/users")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(user))));
    }

    @Test
    public void givenUser_whenCreate_thenStatus201() throws Exception {
        User user = getTestUser();
        user.setId(null);
        User expectedUser = getTestUser();
        mockMvc.perform(
                        post(URI.create("/users"))
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
                        post(URI.create("/users"))
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
                        post(URI.create("/users"))
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
                        post(URI.create("/users"))
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
                        post(URI.create("/users"))
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
                        put(URI.create("/users"))
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
        updatedUser.setId(-1L);
        mockMvc.perform(
                        put(URI.create("/users"))
                                .content(objectMapper.writeValueAsString(updatedUser))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUser_whenGetById_thenStatus200() throws Exception {
        User user = getTestUser();
        repository.save(user);
        mockMvc.perform(
                        get(URI.create("/users/1")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void givenUserWithFailId_whenGetById_thenStatus404() throws Exception {
        User user = getTestUser();
        repository.save(user);
        mockMvc.perform(
                        get(URI.create("/users/-1")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUserAndFriend_whenAddFriend_ThenStatus204AndAddedFriend() throws Exception {
        User user = getTestUser();
        User friend = getTestFriendUser();
        repository.save(user);
        repository.save(friend);
        mockMvc.perform(
                        put(URI.create("/users/1/friends/2")))
                .andExpect(status().isNoContent());
        assertTrue(user.getFriends().contains(2L));
        assertTrue(friend.getFriends().contains(1L));
    }

    @Test
    public void givenUserAndFriendWithFailId_whenAddFriend_ThenStatus404() throws Exception {
        User user = getTestUser();
        repository.save(user);
        mockMvc.perform(
                        put(URI.create("/users/1/friends/-1")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenUserAndFriend_whenDeleteFriend_ThenStatus204AndDeletedFriend() throws Exception {
        User user = getTestUser();
        User friend = getTestFriendUser();
        user.getFriends().add(2L);
        friend.getFriends().add(1L);
        repository.save(user);
        repository.save(friend);
        mockMvc.perform(
                        delete(URI.create("/users/1/friends/2")))
                .andExpect(status().isNoContent());
        assertFalse(user.getFriends().contains(2L));
        assertFalse(friend.getFriends().contains(1L));
    }

    @Test
    public void givenUserAndFriend_whenGetFriends_ThenStatus200() throws Exception {
        User user = getTestUser();
        User friend = getTestFriendUser();
        user.getFriends().add(2L);
        friend.getFriends().add(1L);
        repository.save(user);
        repository.save(friend);
        mockMvc.perform(
                        get(URI.create("/users/1/friends")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(friend))));
    }

    @Test
    public void givenCommonFriends_whenGetCommonFriends_ThenStatus200() throws Exception {
        User user = getTestUser();
        User friend = getTestFriendUser();
        User commonFriend = getTestCommonFriendUser();
        user.getFriends().add(2L);
        user.getFriends().add(3L);
        friend.getFriends().add(1L);
        friend.getFriends().add(3L);
        commonFriend.getFriends().add(1L);
        commonFriend.getFriends().add(2L);
        repository.save(user);
        repository.save(friend);
        repository.save(commonFriend);
        mockMvc.perform(
                        get(URI.create("/users/1/friends/common/2")))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(commonFriend))));
    }

    private User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("mail@mail.ru");
        user.setLogin("dolore");
        user.setName("Nick Name");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        return user;
    }

    private User getTestFriendUser() {
        User friend = new User();
        friend.setId(2L);
        friend.setEmail("friend@mail.ru");
        friend.setLogin("friend");
        friend.setName("friend adipisicing");
        friend.setBirthday(LocalDate.of(1976, 8, 20));
        return friend;
    }

    private User getTestCommonFriendUser() {
        User commonFriend = new User();
        commonFriend.setId(3L);
        commonFriend.setEmail("friend@common.ru");
        commonFriend.setLogin("common");
        commonFriend.setName("common");
        commonFriend.setBirthday(LocalDate.of(2000, 8, 20));
        return commonFriend;
    }

    private User getUpdatedUser() {
        User user = new User();
        user.setId(1L);
        user.setLogin("doloreUpdate");
        user.setName("est adipisicing");
        user.setEmail("mail@yandex.ru");
        user.setBirthday(LocalDate.of(1976, 9, 20));
        return user;
    }
}