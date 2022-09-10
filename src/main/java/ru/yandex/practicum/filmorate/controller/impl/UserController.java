package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.GenericController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.impl.UserService;

@Validated
@RestController
@RequestMapping("/users")
public class UserController extends GenericController<User> {

    @Autowired
    public UserController(UserService service) {
        super(service);
    }
}
