package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;

public class ReleaseDateValidator implements ConstraintValidator<AfterBirthMovie, LocalDate> {
    private static final LocalDate DATE_OF_BIRTH_MOVIE = LocalDate.of(1895, Month.DECEMBER, 28);
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.nonNull(localDate)) {
            return localDate.isAfter(DATE_OF_BIRTH_MOVIE);
        }
        return false;
    }
}
