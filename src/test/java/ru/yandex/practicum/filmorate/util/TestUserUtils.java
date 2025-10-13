package ru.yandex.practicum.filmorate.util;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;

public class TestUserUtils {
    public static User getTestUser() {
        return User.builder()
                .id(1L)
                .email("email1@dotcom")
                .login("login1")
                .name("name1")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
    }

    public static Collection<User> getTestUserCollection() {
        User testUser1 = getTestUser();
        User testUser2 = User.builder()
                .id(2L)
                .email("email2@dotcom")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        User testUser3 = User.builder()
                .id(3L)
                .email("email3@dotcom")
                .login("login3")
                .name("name3")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();
        return Arrays.asList(testUser1, testUser2, testUser3);
    }

}
