package coms309.s1yn3.backend.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.relation.id.GameUserId;

import javax.persistence.*;
import java.util.List;

@Entity
@IdClass(GameUserId.class)
public class GameUserRelation {
	/**
	 * ID of the game associated with this relation.
	 * Defined here in order to allow use as primary key.
	 */
	@Id
	@Column(name = "game")
	private int gameId;

	/**
	 * ID of the user associated with this relation.
	 * Defined here in order to allow use as primary key.
	 */
	@Id
	@Column(name = "user")
	private int userId;

	/**
	 * Game associated with this relation.
	 */
	@ManyToOne(targetEntity = Game.class)
	@JoinColumn(name = "game")
	@MapsId("gameId")
	@JsonBackReference
	private Game game;

	/**
	 * User associated with this relation.
	 */
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user")
	@MapsId("userId")
	@JsonBackReference
	private User user;

	/**
	 * List of relations to the Structures built by this User in this Game.
	 */
	@OneToMany(targetEntity = GameUserStructureRelation.class, mappedBy = "gameUserId")
	private List<GameUserStructureRelation> structureRelations;

	/**
	 * List of relations to the Materials possessed by this User in this Game.
	 */
	@OneToMany(targetEntity = GameUserMaterialRelation.class, mappedBy = "gameUserId")
	private List<GameUserMaterialRelation> materialRelations;

	/**
	 * Empty constructor for use by JPA.
	 */
	public GameUserRelation() {

	}

	/**
	 * Create a new relation between the given Game and User.
	 * @param game
	 * @param user
	 */
	public GameUserRelation(Game game, User user) {
		this.game = game;
		this.user = user;

		gameId = game.getId();
		userId = user.getId();
	}

	/**
	 * @return ID of the Game associated with this relation.
	 */
	public int getGameId() {
		return gameId;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param gameId
	 */
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	/**
	 * @return ID of the User associated with this relation.
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param userId
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return Game associated with this relation.
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param game
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
	 * @return User associated with this relation.
	 */
	public User getUser() {
		return user;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param user
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return List of relations to the Structures built by this User in this Game.
	 */
	public List<GameUserStructureRelation> getStructureRelations() {
		return structureRelations;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param structureRelations
	 */
	public void setStructureRelations(List<GameUserStructureRelation> structureRelations) {
		this.structureRelations = structureRelations;
	}

	/**
	 * @return List of relations to Materials possessed by this User in this Game.
	 */
	public List<GameUserMaterialRelation> getMaterialRelations() {
		return materialRelations;
	}

	/**
	 * For use by JPA. Don't use this.
	 * @param materialRelations
	 */
	public void setMaterialRelations(List<GameUserMaterialRelation> materialRelations) {
		this.materialRelations = materialRelations;
	}
}

