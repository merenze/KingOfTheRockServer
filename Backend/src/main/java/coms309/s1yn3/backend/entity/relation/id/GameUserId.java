package coms309.s1yn3.backend.entity.relation.id;

import coms309.s1yn3.backend.entity.Game;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * This is embedded not in GameUserRelation,
 * but in GameUserStructureRelation
 * and GameUserMaterialRelation
 * for column mapping purposes.
 */
@Embeddable
public class GameUserId implements Serializable {
	@Column(name = "game")
	private int gameId;
	@Column(name = "user")
	private int userId;

//	public GameUserId(int gameId, int userId) {
//		this.gameId = gameId;
//		this.userId = userId;
//	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof GameUserId)) {
			return false;
		}
		GameUserId guid = (GameUserId) o;
		return gameId == guid.gameId &&
				userId == guid.userId;
	}
}
