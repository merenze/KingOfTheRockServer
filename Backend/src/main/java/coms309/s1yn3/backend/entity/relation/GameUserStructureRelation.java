package coms309.s1yn3.backend.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import coms309.s1yn3.backend.entity.repository.Structure;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(GameUserStructureId.class)
public class GameUserStructureRelation {
	@ManyToOne(targetEntity = GameUserRelation.class)

	@EmbeddedId
	private GameUserId gameUserId;

	@Id
	@Column(name = "structure")
	private String structureName;

	/*
		TODO
		This adds a duplicate foreign key to GameUserRelation.

		I haven't found a way around this yet, because it seems complex to use Game and User IDs in this table's keyset,
		while referencing the game-user table and not the games or users themselves.

		Assuming we alter this table's data only through setters and not through raw queries,
		this might not prove to be an issue.
	 */
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

	public GameUserId getGameUserId() {
		return gameUserId;
	}

	public void setGameUserId(GameUserId gameUserId) {
		this.gameUserId = gameUserId;
	}

	public String getStructureName() {
		return structureName;
	}

	public void setStructureName(String structureName) {
		this.structureName = structureName;
	}

	public GameUserRelation getGameUserRelation() {
		return gameUserRelation;
	}

	public void setGameUserRelation(GameUserRelation gameUserRelation) {
		this.gameUserRelation = gameUserRelation;
	}

	public Structure getStructure() {
		return structure;
	}

	public void setStructure(Structure structure) {
		this.structure = structure;
	}
}

class GameUserStructureId implements Serializable {
	private GameUserId gameUserId;

	private String structureName;
}
