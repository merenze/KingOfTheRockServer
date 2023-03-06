package coms309.s1yn3.backend.service.entityprovider;

import coms309.s1yn3.backend.entity.Game;
import coms309.s1yn3.backend.entity.Material;
import coms309.s1yn3.backend.entity.User;
import coms309.s1yn3.backend.entity.relation.GameUserMaterialRelation;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameUserMaterialProviderService extends AbstractEntityProviderService {

	public GameUserMaterialRelation findByGameAndUserAndMaterial(
			Game game,
			User user,
			Material material
	) {
		GameUserMaterialRelation gameUserMaterialRelation;
		try {
			gameUserMaterialRelation = repositories()
					.getGameUserMaterialRepository()
					.findByGameUserRelationAndMaterial(
							repositories()
									.getGameUserRepository()
									.findByGameAndUser(game, user).get(0),
							material
					)
					.get(0);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		} catch (NullPointerException ex) {
			return null;
		}

		return gameUserMaterialRelation;
	}

	public GameUserMaterialRelation findByGameUserRelationAndMaterial(
			GameUserRelation gameUserRelation,
			Material material
	) {
		GameUserMaterialRelation gameUserMaterialRelation;
		try {
			gameUserMaterialRelation =
					repositories()
							.getGameUserMaterialRepository()
							.findByGameUserRelationAndMaterial(gameUserRelation, material)
							.get(0);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
		return gameUserMaterialRelation;
	}

	public List<GameUserMaterialRelation> findByGameAndUser(Game game, User user) {
		GameUserRelation gameUserRelation;
		try {
			gameUserRelation =
					repositories()
					.getGameUserRepository()
					.findByGameAndUser(game, user)
							.get(0);
		} catch (IndexOutOfBoundsException ex) {
			return new ArrayList<>();
		}
		return repositories().getGameUserMaterialRepository().findByGameUserRelation(gameUserRelation);
	}
}
