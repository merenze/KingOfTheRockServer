package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.Structure;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StructureRepository extends JpaRepository<Structure, String> {
	List<Structure> findByName(String name);
}
