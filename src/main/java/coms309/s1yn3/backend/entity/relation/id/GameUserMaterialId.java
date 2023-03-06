package coms309.s1yn3.backend.entity.relation.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class GameUserMaterialId implements Serializable {
	@Column(name = "game")
	private int gameId;

	@Column(name = "user")
	private int userId;

	@Column(name = "material")
	private String materialName;

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof  GameUserMaterialId)) {
			return false;
		}
		GameUserMaterialId gumid = (GameUserMaterialId) o;
		return gameId == gumid.gameId &&
				userId == gumid.userId &&
				materialName.equals(gumid.materialName);
	}
}
