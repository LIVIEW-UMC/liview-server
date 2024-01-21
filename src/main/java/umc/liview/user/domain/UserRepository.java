package umc.liview.user.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public interface UserRepository extends JpaRepository<User,Long> {





}
