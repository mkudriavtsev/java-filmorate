package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.ValidationGroup;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    @Null(groups = ValidationGroup.OnCreate.class)
    @NotNull(groups = ValidationGroup.OnUpdate.class)
    private Long id;

    @Email
    private String email;

    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login;

    private String name;

    @Past
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();
}
