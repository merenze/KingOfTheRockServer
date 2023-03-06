package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.Structure;
import coms309.s1yn3.backend.entity.relation.StructureMaterialRelation;
import coms309.s1yn3.backend.entity.relation.id.StructureMaterialId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StructureMaterialRepository extends JpaRepository<StructureMaterialRelation, StructureMaterialId> {
	List<StructureMaterialRelation> findByStructureAndMaterial(Structure structure, Material material);
}
