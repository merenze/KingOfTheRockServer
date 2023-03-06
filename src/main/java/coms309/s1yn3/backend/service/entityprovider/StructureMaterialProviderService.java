package coms309.s1yn3.backend.service.entityprovider;

import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.Structure;
import coms309.s1yn3.backend.entity.relation.StructureMaterialRelation;
import coms309.s1yn3.backend.service.AbstractEntityManagerService;
import org.springframework.stereotype.Service;

@Service
public class StructureMaterialProviderService extends AbstractEntityProviderService {
	public StructureMaterialRelation findByStructureAndMaterial(Structure structure, Material material) {
		StructureMaterialRelation structureMaterialRelation;
		try {
			structureMaterialRelation =
					repositories()
							.getStructureMaterialRepository()
							.findByStructureAndMaterial(structure, material)
							.get(0);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
		return structureMaterialRelation;
	}
}
