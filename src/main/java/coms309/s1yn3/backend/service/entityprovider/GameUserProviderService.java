package coms309.s1yn3.backend.service.entityprovider;

import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameUserProviderService extends AbstractEntityProviderService {
	@Autowired GameUserMaterialProviderService gameUserMaterialProvider;

	public GameUserRelation findByGameAndUser(Game game, User user) {
		GameUserRelation gameUserRelation;
		try {
			gameUserRelation = repositories().getGameUserRepository().findByGameAndUser(game, user).get(0);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
		return gameUserRelation;
	}

	private void join(GameUserRelation gameUserRelation) {
		Game game = gameUserRelation.getGame();
		User user = gameUserRelation.getUser();
		gameUserRelation.setMaterialRelations(
				gameUserMaterialProvider.findByGameAndUser(game, user)
		);
	}
}
