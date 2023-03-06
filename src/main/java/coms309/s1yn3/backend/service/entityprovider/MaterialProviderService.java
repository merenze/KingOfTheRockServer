package coms309.s1yn3.backend.service.entityprovider;

import coms309.s1yn3.backend.entity.Material;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialProviderService extends AbstractEntityProviderService {
	public Material findByName(String name) {
		Material material;
		try {
			material = repositories().getMaterialRepository().findByName(name).get(0);
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
		return material;
	}

	public List<Material> findAll() {
		return repositories().getMaterialRepository().findAll();
	}
}
