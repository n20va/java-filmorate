package ru.yandex.practicum.filmorate.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.user.JdbcUserRepository;
import ru.yandex.practicum.filmorate.repository.user.UserRowMapper;
import ru.yandex.practicum.filmorate.util.TestUserUtils;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import({JdbcUserRepository.class, UserRowMapper.class})
public class JdbcUserRepositoryTest {
    @Autowired
    private JdbcUserRepository userRepository;

    @Test
    @DisplayName("Should find user by id")
    public void findUserById() {

        User testUser = TestUserUtils.getTestUser();
        Optional<User> userOptional = userRepository.findById(testUser.getId());

        assertThat(userOptional)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(testUser);
    }

    @Test
    @DisplayName("Should find all users")
    public void findUserByName() {
        Collection<User> listOfTestUser = TestUserUtils.getTestUserCollection();

        Collection<User> users = userRepository.findAll();

        assertThat(users)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(listOfTestUser);
    }

    @Test
    @DisplayName("Should save user")
    public void saveUser() {
        User testUser = User.builder()
                .email("user@email")
                .login("userLogin")
                .name("Username")
                .birthday(LocalDate.of(2020, 1, 1))
                .build();

        User savedUser = userRepository.save(testUser);

        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());

        assertThat(retrievedUser)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(testUser);
    }

    @Test
    @DisplayName("Should delete user by id")
    public void deleteUser() {
        User testUser = TestUserUtils.getTestUser();

        userRepository.deleteById(testUser.getId());

        Optional<User> retrievedUser = userRepository.findById(testUser.getId());

        assertThat(retrievedUser)
                .isEmpty();
    }

    @Test
    @DisplayName("Should update user")
    public void updateUser() {
        User testUser = TestUserUtils.getTestUser();
        testUser.setLogin("newLogin");

        userRepository.update(testUser);

        Optional<User> retrievedUser = userRepository.findById(testUser.getId());

        assertThat(retrievedUser)
                .isPresent()
                .get()
                .usingRecursiveComparison()
                .isEqualTo(testUser);

    }

    @Test
    @DisplayName("Should find all friends")
    public void findAllFriends() {
        Collection<User> listOfTestUser = TestUserUtils.getTestUserCollection();
        Collection<User> firstUserFriends = listOfTestUser.stream()
                .filter(user -> user.getId() != 1)
                .toList();

        Collection<User> retrievedFirstUserFriends = userRepository.findAllFriends(1);

        assertThat(retrievedFirstUserFriends)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(firstUserFriends);
    }

    @Test
    @DisplayName("Should find common friends")
    public void saveFriend() {
        Collection<User> listOfTestUser = TestUserUtils.getTestUserCollection();
        Collection<User> firstAndSecondUserCommonFriends = listOfTestUser.stream()
                .filter(user -> user.getId() != 1 && user.getId() != 2)
                .toList();

        Collection<User> retrievedCommonFriends = userRepository.findAllCommonFriends(1, 2);

        assertThat(retrievedCommonFriends)
                .isNotNull()
                .isNotEmpty()
                .usingRecursiveComparison()
                .isEqualTo(firstAndSecondUserCommonFriends);
    }
}
