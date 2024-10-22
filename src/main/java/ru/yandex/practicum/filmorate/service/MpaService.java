package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.inDatabase.InDbMpaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MpaService {

    private final InDbMpaRepository mpaRepository;

    public List<Mpa> getAll() {
        return mpaRepository.getAll();
    }

    public Mpa getById(long id) {
        return mpaRepository.getById(id).orElseThrow(() -> new ValidationException("Rating c ID - " + id + ", не найден."));
    }

}
