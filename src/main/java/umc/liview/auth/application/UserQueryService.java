package umc.liview.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.auth.domain.UserRepository;
import umc.liview.user.domain.User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<User> findUserForOAuth(String email) {
        return userRepository.findByEmail(email);
    }
}
