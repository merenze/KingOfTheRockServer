package coms309.s1yn3.backend.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@IdClass(GameUserId.class)
public class GameUserRelation {
	@Id
	@Column(name = "game")
	private int gameId;

	@Id
	@Column(name = "user")
	private int userId;

	@ManyToOne(targetEntity = Game.class)
	@JoinColumn(name = "user")
	@MapsId("gameId")
	@JsonBackReference
	private Game game;

	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "game")
	@MapsId("userId")
	@JsonBackReference
	private User user;

	/**
	 * Relations to the structures built by this user in this game.
	 */
	@OneToMany(targetEntity = GameUserStructureRelation.class, mappedBy = "gameUserId")
	private List<GameUserStructureRelation> gameUserStructureRelations;


	/**
	 * Empty constructor for use by JPA.
	 */
	public GameUserRelation() {

	}

	/**
	 * Create a new Game-User relation between the given game and user.
	 * @param game
	 * @param user
	 */
	public GameUserRelation(Game game, User user) {
		this.game = game;
		this.user = user;
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

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return Relations to the structures built by this user in this game.
	 */
	public List<GameUserStructureRelation> getGameUserStructureRelations() {
		return gameUserStructureRelations;
	}

	public void setGameUserStructureRelations(List<GameUserStructureRelation> gameUserStructureRelations) {
		this.gameUserStructureRelations = gameUserStructureRelations;
	}
}

@Embeddable
class GameUserId implements Serializable {
	private int gameId;
	private int userId;

	public GameUserId(int gameId, int userId) {
		this.gameId = gameId;
		this.userId = userId;
	}
}
