package umc.liview.auth.domain;

import umc.liview.user.domain.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findById(long userId);

    Optional<User> findByEmail(String email);
}
