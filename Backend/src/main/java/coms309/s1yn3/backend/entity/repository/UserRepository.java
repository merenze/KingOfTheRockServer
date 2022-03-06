package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
	User findById(int id);

	@Transactional
	void deleteById(int id);

	@Query("SELECT new User(u.id,  u.email, u.username, u.isAdmin) FROM User u " +
			"LEFT JOIN Password p ON p.user = u.id " +
			"WHERE u.username = ?1 AND p.password = ?2")
	List<User> getUsers(String username, String password);

	@Query("SELECT new User(u.id, u.email, u.username,u.isAdmin) FROM User u where u.username = ?1")
	List<User> getUsersByUsername(String username);

	@Query("SELECT new User(u.id, u.email, u.username, u.isAdmin) FROM User u where u.email = ?1")
	List<User> getUsersByEmail(String email);

	@Query("SELECT new User(u.id, u.email, u.username,u.isAdmin) FROM User u where u.username = ?1 OR u.email = ?1")
	List<User> getUsersByUsernameOrEmail(String usernameOrEmail);

	@Query("SELECT new User(u.id, u.email, u.username, u.isAdmin) FROM User u where u.username LIKE %?1%")
	List<User> searchUsername(String username);
}
