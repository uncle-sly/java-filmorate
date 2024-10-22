package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FilmGenre {

    @NotNull
    private Long filmId;
    @NotNull
    private Long genreId;

}
