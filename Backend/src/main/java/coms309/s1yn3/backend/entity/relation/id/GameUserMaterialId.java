package coms309.s1yn3.backend.entity.relation.id;

import java.io.Serializable;

public class GameUserMaterialId implements Serializable {
	private GameUserId gameUserId;
	private String materialName;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof  GameUserMaterialId)) {
			return false;
		}
		GameUserMaterialId gumid = (GameUserMaterialId) o;
		return gameUserId.equals(gumid.gameUserId) &&
				materialName.equals(gumid.materialName);
	}
}
