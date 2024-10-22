package ru.yandex.practicum.filmorate.repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    Optional<Film> get(long filmId);

    List<Film> getAll();

    Film save(Film film);

    Film update(Film film);

    void addLike(Film film, User user);

    boolean deleteLike(Film film, User user);

    List<Film> getPopular(long count);

}
