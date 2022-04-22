package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import coms309.s1yn3.backend.entity.relation.id.GameUserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameUserRelation, GameUserId> {
}
