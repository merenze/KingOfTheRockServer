package coms309.s1yn3.backend.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.relation.id.GameUserMaterialId;

import javax.persistence.*;

@Entity
@IdClass(GameUserMaterialId.class)
public class GameUserMaterialRelation {
	@Column(name = "game", insertable = false, updatable = false)
	@Id
	int gameId;

	@Column(name = "user", insertable = false, updatable = false)
	@Id
	int userId;

	@Column(name = "material")
	@Id
	String materialName;

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
	 * When this number is "rolled",
	 */

	/**
	 * Default constructor for use by JPA.
	 */
	public GameUserMaterialRelation() {

	}

	/**
	 * Empty constructor for use by JPA.
	 */
	public GameUserMaterialRelation(GameUserRelation gameUserRelation, Material material) {
		this.gameUserRelation = gameUserRelation;
		this.gameId = gameUserRelation.getGameId();
		this.userId = gameUserRelation.getUserId();
		this.material = material;
		this.materialName = material.getName();
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

