package coms309.s1yn3.backend.entity.relation.id;

import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.User;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
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

	public GameUserId() {

	}

	public GameUserId(Game game, User user) {
		gameId = game.getId();
		userId = user.getId();
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

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
