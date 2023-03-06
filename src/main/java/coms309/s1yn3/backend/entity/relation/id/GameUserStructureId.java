package coms309.s1yn3.backend.entity.relation.id;

import javax.persistence.Column;
import java.io.Serializable;

public class GameUserStructureId implements Serializable {
	@Column(name = "game")
	private int gameId;

	@Column(name = "user")
	private int userId;

	@Column(name = "structure")
	private String structureName;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof  GameUserStructureId)) {
			return false;
		}
		GameUserStructureId gusid = (GameUserStructureId) o;
		return gameId == gusid.gameId &&
				userId == gusid.userId &&
				structureName.equals(gusid.structureName);
	}
}
