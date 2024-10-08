package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.util.List;


@RestController()
@RequestMapping("/films")
@Slf4j
@Validated
@RequiredArgsConstructor
public class FilmController {

    private final FilmService service;

    @PostMapping
    public Film save(@Valid @RequestBody Film film) {
        log.info("POST /films --> Create Film: {} - started", film);
        service.save(film);
        log.info("POST /films <-- Create Film: {} - ended", film);

        return film;
    }

    @PutMapping
    public Film update(@Valid @Validated(OnUpdate.class) @RequestBody Film film) {
        log.info("PUT /films --> Update Film: {} - started", film);
        Film updatedFilm = service.update(film);
        log.info("PUT /films <-- Update Film: {} - ended", film);
        return updatedFilm;
    }

    @GetMapping
    public List<Film> getAll() {
        log.info("GET /films <--> Get all films");
        return service.getAll();
    }

    @GetMapping("{id}")
    public Film getById(@PathVariable("id") @Positive long id) {
        log.info("GET /films/id <--> Get film by id");
        return service.getById(id);
    }

    //    пользователь ставит лайк фильму.
    @PutMapping("{id}/like/{userId}")
    public void addLike(@PathVariable("id") long filmId, @PathVariable("userId") long userId) {
        log.info("PUT /films/filmId/like/userId --> User {} adding like for Film {} - started", userId, filmId);
        service.addLike(filmId, userId);
        log.info("PUT /films/filmId/like/userId <-- User {} adding like for Film {} - ended", userId, filmId);

    }

    //    пользователь удаляет лайк
    @DeleteMapping("{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") long filmId, @PathVariable("userId") long userId) {
        log.info("DELETE /films/filmId/like/userId --> User {} deleting like for Film {} - started", userId, filmId);
        service.deleteLike(filmId, userId);
        log.info("DELETE /films/filmId/like/userId <-- User {} deleting like for Film {} - ended", userId, filmId);

    }

    //    возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, верните первые 10.
    @GetMapping("/popular")
    public List<Film> getPopular(@Positive @RequestParam(defaultValue = "10") long count) {

        log.info("GET /films/popular?count --> getting {} popular Films - started", count);
        List<Film> popularFilms = service.getPopular(count);
        log.info("GET /films/popular?count --> getting {} popular Films - ended", count);

        return popularFilms;
    }

}