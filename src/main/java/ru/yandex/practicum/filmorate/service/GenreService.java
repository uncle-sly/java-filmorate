package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbGenreRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreService {

    private final InDbGenreRepository genreRepository;


    public List<Genre> getAll() {
        return genreRepository.getAll();
    }

    public Genre getById(long id) {
        return genreRepository.getById(id)
                .orElseThrow(() -> new ValidationException("Жанры для фильма c ID - " + id + ", не найдены."));
    }
}
