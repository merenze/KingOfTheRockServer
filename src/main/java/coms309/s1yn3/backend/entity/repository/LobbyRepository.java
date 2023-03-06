package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LobbyRepository extends JpaRepository<Lobby, String> {
	@Query("SELECT new Lobby(l.code) FROM Lobby l WHERE l.code = ?1")
	List<Lobby> findByCode(String code);
}
