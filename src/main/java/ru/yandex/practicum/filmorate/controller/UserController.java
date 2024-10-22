package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validation.OnUpdate;

import java.util.List;
import java.util.Set;

@RestController()
@RequestMapping("/users")
@Slf4j
@Validated // необходимо добавить @Validated в контроллер на уровне класса, чтобы проверять параметры метода.
            // В этом случае аннотация @Validated устанавливается на уровне класса, даже если она присутствует на методах.
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @GetMapping("{id}")
    public User getById(@PathVariable("id") @Positive long id) {
        log.info("GET /users/id --> Get user by id");
        return service.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User save(@Valid @RequestBody final User user) {
        log.info("POST / Users --> Create User: {} - started", user);
        User savedUser = service.save(user);
        log.info("POST / Users <-- Create User: {} - ended", user);
        return savedUser;
    }

    @PutMapping
    public User update(@Valid @Validated(OnUpdate.class) @RequestBody final User user) {
        log.info("PUT / Users --> Update User: {} - started", user);
        User updatedUser = service.update(user);
        log.info("PUT / Users <-- Update User: {} - ended", user);
        return updatedUser;
    }

    //    добавление в друзья
    @PutMapping("{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") long userId, @PathVariable("friendId") long friendId,
                          @RequestParam(defaultValue = "false") boolean isConfirmed) {
        log.info("PUT /users/userId/friends/friendId --> Adding friend {} for user {} - started", friendId, userId);
        service.addFriend(userId, friendId, isConfirmed);
        log.info("PUT /users/userId/friends/friendId <-- Adding friend {} for user {} - ended", friendId, userId);
    }


    //    удаление из друзей
    @DeleteMapping("{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") long userId, @PathVariable("friendId") long friendId) {
        log.info("DELETE /users/userId/friends/friendId --> Deleting friend {} for user {} - started", friendId, userId);
        service.deleteFriend(userId, friendId);
        log.info("DELETE /users/userId/friends/friendId <-- Deleting friend {} for user {} - ended", friendId, userId);
    }


    //    возвращаем список пользователей, являющихся его друзьями
    @GetMapping("{id}/friends")
    public Set<User> getAllFriends(@PathVariable("id") long userId) {
        log.info("GET /users/userId/friends --> Getting friends list for user {} - started", userId);
        Set<User> friendsList = service.getFriends(userId);
        log.info("GET /users/userId/friends <-- Getting friends list for user {} - ended", userId);

        return friendsList;
    }


    //    список друзей, общих с другим пользователем
    @GetMapping("{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") long userId, @PathVariable("otherId") long otherId) {
        log.info("GET /users/userId/friends --> Getting common friends list for user {} with other user {} - started", userId, otherId);
        List<User> commonFriendsList = service.getCommonFriends(userId, otherId);
        log.info("GET /users/userId/friends <-- Getting common friends list for user {} with other user {} - ended", userId, otherId);
        return commonFriendsList;
    }
}

