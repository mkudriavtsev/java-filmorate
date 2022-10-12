package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Genre implements Comparable<Genre>{

    private Long id;
    private String name;

    @Override
    public int compareTo(Genre genre) {
        return Long.compare(this.id, genre.getId());
    }
}
