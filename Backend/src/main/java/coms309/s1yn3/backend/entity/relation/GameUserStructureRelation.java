package coms309.s1yn3.backend.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import coms309.s1yn3.backend.entity.repository.Structure;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(GameUserStructureId.class)
public class GameUserStructureRelation {
	/**
	 * This definition allows the key (game, user) to map to the game-user-relation.
	 */
	@EmbeddedId
	private GameUserId gameUserId;

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
			@JoinColumn(name = "game", referencedColumnName = "game"),
			@JoinColumn(name = "user", referencedColumnName = "user")
	})
	@MapsId("gameUserId")
	@JsonBackReference
	private GameUserRelation gameUserRelation;

	/**
	 * Structure.
	 */
	@ManyToOne
	@JoinColumn(name = "structure")
	@MapsId("structureName")
	private Structure structure;

	/**
	 * Embedded GameUserId used for hacky mapping.
	 * For use by JPA.
	 * @return
	 */
	public GameUserId getGameUserId() {
		return gameUserId;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param gameUserId
	 */
	public void setGameUserId(GameUserId gameUserId) {
		this.gameUserId = gameUserId;
	}

	/**
	 * @return Name of the structure used in this relation.
	 */
	public String getStructureName() {
		return structureName;
	}

	/**
	 * Please use class constants and don't do anything stupid with this.
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
	}
}

class GameUserStructureId implements Serializable {
	private GameUserId gameUserId;
	private String structureName;
}
