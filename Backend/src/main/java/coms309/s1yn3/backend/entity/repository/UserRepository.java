package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findById(int id);
	@Transactional
	void deleteById(int id);
}
