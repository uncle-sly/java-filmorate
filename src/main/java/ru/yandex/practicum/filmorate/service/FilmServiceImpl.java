package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FkConstraintViolationException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.FilmRepository;
import ru.yandex.practicum.filmorate.repository.UserRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbFilmGenreRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbGenreRepository;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbMpaRepository;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final InDbMpaRepository mpaRepository;
    private final InDbGenreRepository genreRepository;
    private final InDbFilmGenreRepository filmGenreRepository;


    public Film save(Film film) {
        film.setMpa(mpaRepository.getById(film.getMpa().getId())
                .orElseThrow(() -> new FkConstraintViolationException("Рейтинг вне диапазона."))
        );
        filmRepository.save(film);

        if (null != film.getGenres()) {

            film.setGenres(new LinkedHashSet<>(film.getGenres().stream()
                    .map(id -> genreRepository.getById(id.getId())
                            .orElseThrow(() -> new FkConstraintViolationException("Жанр вне диапазона.")))
                    .toList())
            );
            filmGenreRepository.save(film);
        }
        return film;
    }

    public Film update(final Film film) {

        long filmId = film.getId();
        filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));

        return filmRepository.update(film);
    }

    public Film getById(long filmId) {

        Film film = filmRepository.get(filmId).orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));

            film.setMpa(mpaRepository.getById(film.getMpa().getId())
                    .orElseThrow(() -> new FkConstraintViolationException("Рейтинг вне диапазона."))
            );
        film.setGenres(new HashSet<>(filmGenreRepository.getById(film.getId()).stream()
                    .map(id -> genreRepository.getById(id.getGenreId())
                            .orElseThrow(() -> new ValidationException("Жанр не найден.")))
                    .toList()));

        return film;
    }

    public List<Film> getAll() {
        return filmRepository.getAll();
    }

    public void addLike(long filmId, long userId) {

        final Film film = filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));

        final User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        filmRepository.addLike(film, user);
    }

    public void deleteLike(long filmId, long userId) {

        final Film film = filmRepository.get(filmId)
                .orElseThrow(() -> new ValidationException("Фильм c ID - " + filmId + ", не найден."));

        final User user = userRepository.get(userId)
                .orElseThrow(() -> new ValidationException("Пользователь c id: " + userId + " не найден"));

        filmRepository.deleteLike(film, user);
    }

    public List<Film> getPopular(long count) {
        return filmRepository.getPopular(count);
    }
}
