package coms309.s1yn3.backend.service.entityprovider;

import coms309.s1yn3.backend.entity.Game;
import org.springframework.stereotype.Service;

@Service
public class GameProviderService extends AbstractEntityProviderService {
	public Game findById(int id) {
		Game game = repositories().getGameRepository().findById(id);
		join(game);
		return game;
	}

	private void join(Game game) {
		game.setUserRelations(repositories().getGameUserRepository().findByGame(game));
	}
}
