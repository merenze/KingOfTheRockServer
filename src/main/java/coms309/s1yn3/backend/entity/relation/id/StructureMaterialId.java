package coms309.s1yn3.backend.entity.relation.id;

import java.io.Serializable;

public class StructureMaterialId implements Serializable {
	private String structureName;
	private String materialName;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof StructureMaterialId)) {
			return false;
		}
		StructureMaterialId smid = (StructureMaterialId) o;
		return structureName.equals(smid.structureName) &&
				materialName.equals(smid.materialName);
	}
}
