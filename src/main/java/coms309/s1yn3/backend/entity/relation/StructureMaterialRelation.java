package coms309.s1yn3.backend.entity.relation;

import com.fasterxml.jackson.annotation.JsonBackReference;
import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.Structure;
import coms309.s1yn3.backend.entity.relation.id.StructureMaterialId;

import javax.persistence.*;

@Entity
@IdClass(StructureMaterialId.class)
public class StructureMaterialRelation {
	/**
	 * Name of the Structure associated with this relation.
	 */
	@Id
	@Column(name = "structure")
	private String structureName;

	/**
	 * Name of the Material associated with this relation.
	 */
	@Id
	@Column(name = "material")
	private String materialName;

	/**
	 * Structure associated with this relation.
	 */
	@ManyToOne
	@JoinColumn(name = "structure")
	@MapsId("structure")
	@JsonBackReference
	private Structure structure;

	/**
	 * Material associated with this relation.
	 */
	@ManyToOne
	@JoinColumn(name = "material")
	@MapsId("material")
	@JsonBackReference
	private Material material;

	/**
	 * Amount of the Material required for the Structure's recipe.
	 */
	private int amount;

	/**
	 * Empty constructor for use by JPA.
	 */
	public StructureMaterialRelation() {

	}

	/**
	 * For use in populating the initial database.
	 * @param structure Structure associated with this relation.
	 * @param material Material associated with this relation.
	 * @param amount Amount of the Material required for the Structure's recipe.
	 */
	public StructureMaterialRelation(Structure structure, Material material, int amount) {
		this.structure = structure;
		this.structureName = structure.getName();
		this.material = material;
		this.materialName = material.getName();
		this.amount = amount;
	}

	/**
	 * @return Name of the Structure associated with this relation.
	 */
	public String getStructureName() {
		return structureName;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param structureName
	 */
	public void setStructureName(String structureName) {
		this.structureName = structureName;
	}

	/**
	 * @return Name of the Material associated with this relation.
	 */
	public String getMaterialName() {
		return materialName;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param materialName
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	/**
	 * @return Structure associated with this relation.
	 */
	public Structure getStructure() {
		return structure;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param structure
	 */
	public void setStructure(Structure structure) {
		this.structure = structure;
	}

	/**
	 * @return Material associate with this relation.
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param material
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	/**
	 * @return Amount of the Material required for the Structure's recipe.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * For use by JPA.
	 * Don't use this.
	 * @param amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
}

