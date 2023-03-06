package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.Structure;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import coms309.s1yn3.backend.entity.relation.GameUserStructureRelation;
import coms309.s1yn3.backend.entity.relation.id.GameUserStructureId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameUserStructureRepository extends JpaRepository<GameUserStructureRelation, GameUserStructureId> {
	List<GameUserStructureRelation> findByGameUserRelation(GameUserRelation gameUserRelation);

	List<GameUserStructureRelation> findByGameUserRelationAndStructure(GameUserRelation gameUserRelation, Structure structure);
}
