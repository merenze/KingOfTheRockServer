package coms309.s1yn3.backend.entity.repository;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Structure {
	/**
	 * The unique name of this structure.
	 */
	@Id
	private String name;

	/**
	 * The amount of points this structure is worth.
	 */
	private int points;

	/**
	 * For use by JPA
	 */
	public Structure() {

	}

	/**
	 * Add a structure with the given name.
	 * @param name
	 */
	public Structure(String name, int points) {
		this.name = name;
		this.points = points;
	}

	/**
	 * @return The unique name of this structure.
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The amount of points this structure is worth.
	 */
	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
}
