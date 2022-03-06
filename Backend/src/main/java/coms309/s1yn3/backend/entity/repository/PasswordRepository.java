package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.Password;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordRepository extends JpaRepository<Password, Integer> {
	public Password findById(int id);
}
