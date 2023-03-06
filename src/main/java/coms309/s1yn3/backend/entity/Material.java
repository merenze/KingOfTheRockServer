package coms309.s1yn3.backend.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Material {
	/**
	 * Unique name of this material.
	 */
	@Id
	private String name;

	/**
	 * For use by JPA.
	 */
	public Material() {

	}

	/**
	 * Adds a material with the given name.
	 * Use class constants. Be smart.
	 * @param name
	 */
	public Material(String name) {
		this.name = name;
	}

	/**
	 * @return Unique name of this material.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name Unique name of this material.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Material)) {
			return false;
		}
		Material material = (Material) o;
		return name.equals(material.name);
	}
}
