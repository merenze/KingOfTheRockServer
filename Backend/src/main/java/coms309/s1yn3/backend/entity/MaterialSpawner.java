package coms309.s1yn3.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;

import javax.persistence.*;

@Entity
public class MaterialSpawner {
	/**
	 * Unique ID. Not necessary, but an
	 * Arbitrary requirement of JPA
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "game")
	private int gameId;

	@Column(name = "user")
	private int userId;

	@Column(name = "material")
	private String materialName;

	/**
	 * When this number is "rolled", the player collects that resource.
	 */
	private int spawnNumber;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "game", referencedColumnName = "game", insertable = false, updatable = false),
			@JoinColumn(name = "user", referencedColumnName = "user", insertable = false, updatable = false),
	})
	@JsonBackReference
	private GameUserRelation gameUserRelation;

	@ManyToOne(targetEntity = Material.class)
	@JoinColumn(name = "material", referencedColumnName = "name", insertable = false, updatable = false)
	private Material material;

	/**
	 * Empty constructor for use by JPA.
	 */
	public MaterialSpawner() {

	}

	public MaterialSpawner(GameUserRelation gameUserRelation, Material material, int spawnNumber) {
		this.gameUserRelation = gameUserRelation;
		this.gameId = gameUserRelation.getGameId();
		this.userId = gameUserRelation.getUserId();
		this.material = material;
		this.materialName = material.getName();
		this.spawnNumber = spawnNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getSpawnNumber() {
		return spawnNumber;
	}

	public void setSpawnNumber(int spawnNumber) {
		this.spawnNumber = spawnNumber;
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
