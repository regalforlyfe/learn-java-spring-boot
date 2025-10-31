package creotech.crud.repository;

import creotech.crud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<User, String> {

    Optional<User> findFirstByToken(String token);

    Optional<User> findFirstByEmail(String email);

    boolean existsByEmail(String email);
}
