package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.MaterialSpawner;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialSpawnerRepository extends JpaRepository<MaterialSpawner, Integer> {
	List<MaterialSpawner> findByGameUserRelation(GameUserRelation gameUserRelation);

	List<MaterialSpawner> findByGameUserRelationAndSpawnNumber(GameUserRelation gameUserRelation, int spawnNumber);
}
