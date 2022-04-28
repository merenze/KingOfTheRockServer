package coms309.s1yn3.backend.entity.relation.id;

import java.io.Serializable;

public class GameUserStructureId implements Serializable {
	private GameUserId gameUserId;
	private String structureName;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof  GameUserStructureId)) {
			return false;
		}
		GameUserStructureId gusid = (GameUserStructureId) o;
		return gameUserId.equals(gusid.gameUserId) &&
				structureName.equals(gusid.structureName);
	}
}
