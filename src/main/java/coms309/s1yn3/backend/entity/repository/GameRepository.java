package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer> {
	Game findById(int id);
}
