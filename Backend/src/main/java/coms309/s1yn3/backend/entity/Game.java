package coms309.s1yn3.backend.entity;

import coms309.s1yn3.backend.entity.relation.GameUserRelation;

import javax.persistence.*;
import java.util.List;

@Entity
public class Game {
	/**
	 * Unique ID for this game.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	/**
	 * List of game-user relations associated with this game.
	 */
	@OneToMany(targetEntity = GameUserRelation.class, mappedBy = "game")
	private List<GameUserRelation> userRelations;

	/**
	 * @return Unique ID for this game.
	 */
	public int getId() {
		return id;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param id Unique ID for this game.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return List of game-user relations associated with this game.
	 */
	public List<GameUserRelation> getUserRelations() {
		return userRelations;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param userRelations
	 */
	public void setUserRelations(List<GameUserRelation> userRelations) {
		this.userRelations = userRelations;
	}
}
