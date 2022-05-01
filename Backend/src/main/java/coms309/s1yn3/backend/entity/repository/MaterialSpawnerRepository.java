package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.MaterialSpawner;
import coms309.s1yn3.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialSpawnerRepository extends JpaRepository<Material, Integer> {
	List<MaterialSpawner> findByGameAndUser(Game game, User user);

	List<MaterialSpawner> findByGameAndUserAndSpawnNumber(Game game, User user, int spawnNumber);
}
