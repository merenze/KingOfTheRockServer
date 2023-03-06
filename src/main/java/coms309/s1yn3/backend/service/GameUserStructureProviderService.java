package coms309.s1yn3.backend.service;

import coms309.s1yn3.backend.entity.Structure;
import coms309.s1yn3.backend.entity.relation.GameUserRelation;
import coms309.s1yn3.backend.entity.relation.GameUserStructureRelation;
import coms309.s1yn3.backend.service.entityprovider.AbstractEntityProviderService;
import org.springframework.stereotype.Service;

@Service
public class GameUserStructureProviderService extends AbstractEntityProviderService {
	public GameUserStructureRelation findByGameUserRelationAndStructure(
			GameUserRelation gameUserRelation,
			Structure structure
	) {
		GameUserStructureRelation gameUserStructureRelation;
		try {
			gameUserStructureRelation =
					repositories()
							.getGameUserStructureRepository()
							.findByGameUserRelationAndStructure(gameUserRelation, structure)
							.get(0);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
		return gameUserStructureRelation;
	}
}
