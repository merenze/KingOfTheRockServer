package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.relation.GameUserMaterialRelation;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import coms309.s1yn3.backend.entity.relation.id.GameUserMaterialId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameUserMaterialRepository extends JpaRepository<GameUserMaterialRelation, GameUserMaterialId> {
	List<GameUserMaterialRelation> findByGameUserRelationAndMaterial(GameUserRelation gameUserRelation, Material material);

	List<GameUserMaterialRelation> findByGameUserRelation(GameUserRelation gameUserRelation);
}
