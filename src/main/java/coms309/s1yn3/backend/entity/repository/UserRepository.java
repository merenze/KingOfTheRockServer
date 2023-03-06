package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.Lobby;
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

	@Query("SELECT u FROM User u " +
			"LEFT JOIN Password p ON p.user = u.id " +
			"LEFT JOIN FETCH Lobby l ON u.lobby = l.code " +
			"WHERE u.username = ?1 AND p.password = ?2")
	List<User> findByUsernameAndPassword(String username, String password);

	List<User> findByUsername(String username);

	List<User> findByEmail(String email);

	List<User> findByUsernameOrEmail(String username, String email);

	List<User> findByUsernameContaining(String username);

	List<User> findByLobby(Lobby lobby);
}
