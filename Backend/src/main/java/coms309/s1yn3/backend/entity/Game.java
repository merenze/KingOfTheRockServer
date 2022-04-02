package coms309.s1yn3.backend.entity;

import coms309.s1yn3.backend.entity.relation.GameUserRelation;

import javax.persistence.*;
import java.util.List;

@Entity
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@OneToMany(targetEntity = GameUserRelation.class, mappedBy = "game")
	private List<GameUserRelation> userRelations;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<GameUserRelation> getUserRelations() {
		return userRelations;
	}

	public void setUserRelations(List<GameUserRelation> userRelations) {
		this.userRelations = userRelations;
	}
}
