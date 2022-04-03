package coms309.s1yn3.backend.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import coms309.s1yn3.backend.entity.Material;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(GameUserMaterialId.class)
public class GameUserMaterialRelation {
	@EmbeddedId
	private GameUserId gameUserId;

	@Id
	@Column(name = "game", insertable = false, updatable = false)
	private int gameId;

	@Id
	@Column(name = "user", insertable = false, updatable = false)
	private int userId;

	@Id
	@Column(name = "material")
	private String materialName;

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
	 * Material.
	 */
	@ManyToOne
	@JoinColumn(name = "material")
	@MapsId("materialName")
	private Material material;

	public GameUserId getGameUserId() {
		return gameUserId;
	}

	public void setGameUserId(GameUserId gameUserId) {
		this.gameUserId = gameUserId;
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

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	public GameUserRelation getGameUserRelation() {
		return gameUserRelation;
	}

	public void setGameUserRelation(GameUserRelation gameUserRelation) {
		this.gameUserRelation = gameUserRelation;
	}

	public Material getMaterial() {
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}
}

class GameUserMaterialId implements Serializable {
	private GameUserId gameUserId;
	private String materialName;
}
