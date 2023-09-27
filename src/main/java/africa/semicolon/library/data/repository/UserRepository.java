package africa.semicolon.library.data.repository;

import africa.semicolon.library.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
