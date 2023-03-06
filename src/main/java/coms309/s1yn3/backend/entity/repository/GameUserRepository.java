package coms309.s1yn3.backend.entity.repository;

import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import coms309.s1yn3.backend.entity.relation.id.GameUserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameUserRepository extends JpaRepository<GameUserRelation, GameUserId> {
	List<GameUserRelation> findByGameAndUser(Game game, User user);

	List<GameUserRelation> findByGame(Game game);

	List<GameUserRelation> findByUser(User user);
}
