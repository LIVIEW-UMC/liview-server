package umc.liview.auth.infra;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import umc.liview.auth.domain.UserRepository;
import umc.liview.user.domain.User;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserJpaRepository implements UserRepository {

    private final EntityManager em;

    @Override
    public void save(User user) {
        em.persist(user);
    }

    @Override
    public Optional<User> findById(long userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> users = em.createQuery("select u from User u" +
                        " where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();

        return users.stream().findAny();
    }
}
