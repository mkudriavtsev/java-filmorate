package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.GenericController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.impl.FilmService;

@Validated
@RestController
@RequestMapping("/films")
public class FilmController extends GenericController<Film> {

    @Autowired
    public FilmController(FilmService service) {
        super(service);
    }
}
