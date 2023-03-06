package coms309.s1yn3.backend.entity;

import coms309.s1yn3.backend.entity.relation.StructureMaterialRelation;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Structure {
	/**
	 * The unique name of this structure.
	 */
	@Id
	private String name;

	/**
	 * List of structure-material relations associated with this Structure.
	 */

	@OneToMany(targetEntity = StructureMaterialRelation.class, mappedBy = "structure")
	private List<StructureMaterialRelation> materialRelations;

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

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return List of structure-material relations associated with this Structure.
	 */
	public List<StructureMaterialRelation> getMaterialRelations() {
		return materialRelations;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param materialRelations
	 */
	public void setMaterialRelations(List<StructureMaterialRelation> materialRelations) {
		this.materialRelations = materialRelations;
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
