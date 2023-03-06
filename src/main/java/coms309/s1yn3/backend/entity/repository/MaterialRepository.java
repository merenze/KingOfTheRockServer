package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, String> {
	List<Material> findByName(String name);

	List<Material> findAll();
}
