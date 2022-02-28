package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootApplication
public interface UserRepository extends JpaRepository<User, Integer> {
	User findById(int id);
	@Transactional
	void deleteById(int id);

	@Query("SELECT new User(u.id,  u.email, u.username, u.password, u.isAdmin) FROM User u WHERE u.username = ?1 AND u.password = ?2")
	List<User> getUsers(String username, String password);

	@Query("SELECT new User(u.id, u.email, u.username, u.password, u.isAdmin) FROM User u where u.username = ?1")
	List<User> getUsersByUsername(String username);

	@Query("SELECT new User(u.id, u.email, u.username, u.password, u.isAdmin) FROM User u where u.username = ?1")
	List<User> getUsersByEmail(String email);

	@Query("SELECT new User(u.id, u.email, u.username, u.password, u.isAdmin) FROM User u where u.username = ?1 OR u.email = ?1")
	List<User> getUsersByUsernameOrEmail(String usernameOrEmail);
}
