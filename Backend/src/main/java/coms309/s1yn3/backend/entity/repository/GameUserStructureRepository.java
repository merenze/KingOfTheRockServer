package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.relation.GameUserStructureRelation;
import coms309.s1yn3.backend.entity.relation.id.GameUserStructureId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameUserStructureRepository extends JpaRepository<GameUserStructureRelation, GameUserStructureId> {
}
