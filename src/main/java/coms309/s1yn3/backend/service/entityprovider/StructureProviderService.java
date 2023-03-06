package coms309.s1yn3.backend.service.entityprovider;

import coms309.s1yn3.backend.entity.Structure;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StructureProviderService extends AbstractEntityProviderService {
	public Structure findByName(String name) {
		Structure structure;
		try {
			structure = repositories().getStructureRepository().findByName(name).get(0);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
		return structure;
	}

	public List<Structure> findAll() {
		return repositories().getStructureRepository().findAll();
	}
}
