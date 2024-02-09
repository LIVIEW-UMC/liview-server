package umc.liview.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.liview.auth.domain.UserRepository;
import umc.liview.user.domain.User;

@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;

    @Transactional
    public User saveOAuthUser(User user) {
        userRepository.save(user);
        return user;
    }
}
