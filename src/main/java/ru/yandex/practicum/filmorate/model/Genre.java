package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Genre {

    private Long id;

    @NotEmpty
    private String name;


}
