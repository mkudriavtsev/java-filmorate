package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;
import ru.yandex.practicum.filmorate.validation.AfterBirthMovie;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film{

    @Null(groups = ValidationGroup.OnCreate.class)
    @NotNull(groups = ValidationGroup.OnUpdate.class)
    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @AfterBirthMovie
    private LocalDate releaseDate;

    @DurationMin(minutes = 0)
    private Duration duration;

    private final Set<Long> likes = new HashSet<>();
}
