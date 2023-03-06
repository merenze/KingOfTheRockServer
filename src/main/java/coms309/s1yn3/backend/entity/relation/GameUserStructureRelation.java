package coms309.s1yn3.backend.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import coms309.s1yn3.backend.entity.Structure;
import coms309.s1yn3.backend.entity.relation.id.GameUserId;
import coms309.s1yn3.backend.entity.relation.id.GameUserStructureId;

import javax.persistence.*;

@Entity
@IdClass(GameUserStructureId.class)
public class GameUserStructureRelation {
	/**
	 * ID of the game in the game-user relation.
	 * Definition required in order to override the column name.
	 */
	@Id
	@Column(name = "game", insertable = false, updatable = false)
	private int gameId;

	/**
	 * ID of the user in the game-user relation.
	 * Definition required in order to override the column name.
	 */
	@Id
	@Column(name = "user", insertable = false, updatable = false)
	private int userId;

	/**
	 * Name of the structure used in this relation.
	 */
	@Id
	@Column(name = "structure")
	private String structureName;

	/**
	 * Game-User relation associated with this relation.
	 */
	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "game", referencedColumnName = "game", insertable = false, updatable = false),
			@JoinColumn(name = "user", referencedColumnName = "user", insertable = false, updatable = false)
	})
	@JsonBackReference
	private GameUserRelation gameUserRelation;

	/**
	 * Structure.
	 */
	@ManyToOne
	@JoinColumn(name = "structure", insertable = false, updatable = false)
	@MapsId("structureName")
	@JsonBackReference
	private Structure structure;

	/**
	 * Amount of this structure the User has built in this game.
	 */
	private int amount;

	/**
	 * Empty constructor for use by JPA.
	 */
	public GameUserStructureRelation() {

	}

	/**
	 *
	 * @param gameUserRelation GameUserRelation associated with this relation.
	 * @param structure Structure associated with this relation.
	 */
	public GameUserStructureRelation(GameUserRelation gameUserRelation, Structure structure) {
		// TODO GameUserMaterial constructor, setters should match this one
		setGameUserRelation(gameUserRelation);
		setStructure(structure);
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
	 * @param gameId ID of the Game associated with this relation.
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
	 * @return Name of the Structure used in this relation.
	 */
	public String getStructureName() {
		return structureName;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param structureName Name of the structure used in this relation.
	 */
	public void setStructureName(String structureName) {
		this.structureName = structureName;
	}

	/**
	 * @return GameUserRelation associated with this relation.
	 */
	public GameUserRelation getGameUserRelation() {
		return gameUserRelation;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param gameUserRelation
	 */
	public void setGameUserRelation(GameUserRelation gameUserRelation) {
		this.gameUserRelation = gameUserRelation;
		this.gameId = gameUserRelation.getGameId();
		this.userId = gameUserRelation.getUserId();
	}

	/**
	 * @return Structure associated with this relation.
	 */
	public Structure getStructure() {
		return structure;
	}

	/**
	 * @param structure Structure associated with this relation.
	 */
	public void setStructure(Structure structure) {
		this.structure = structure;
		this.structureName = structure.getName();
	}

	/**
	 * @return Amount of this structure the User has built in this Game.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount Amount of this structure the User has built in this Game.
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * Increment the amount of this structure the User has built.
	 */
	public void incrementAmount() {
		this.amount += 1;
	}
}

