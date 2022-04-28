package coms309.s1yn3.backend.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.relation.id.GameUserId;
import coms309.s1yn3.backend.entity.relation.id.GameUserMaterialId;

import javax.persistence.*;

@Entity
@IdClass(GameUserMaterialId.class)
public class GameUserMaterialRelation {
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
	 * Name of the material used in this relation.
	 */
	@Id
	@Column(name = "material")
	private String materialName;

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

	/**
	 * Amount of this structure the User has built in this game.
	 */
	private int amount;

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
	 * @return Name of the Material used in this relation.
	 */
	public String getMaterialName() {
		return materialName;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param materialName Name of the Material used in this relation.
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
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
	 * @return Material associated with this relation.
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @param material Structure associated with this relation.
	 */
	public void setMaterial(Material material) {
		this.material = material;
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
	 * Increase the amount of material this player possesses.
	 * Negative values are allowed.
	 * @param amount Amount by which to increase.
	 */
	public void add(int amount) {
		this.amount += amount;
	}

	/**
	 * Decrease the amount of material this player possesses.
	 * Negative values are allowed.
	 * @param amount Amount by which to increase.
	 */
	public void remove(int amount) {
		this.amount -= amount;
	}
}

