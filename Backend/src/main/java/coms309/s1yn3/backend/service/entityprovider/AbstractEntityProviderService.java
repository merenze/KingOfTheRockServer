package coms309.s1yn3.backend.service.entityprovider;

import coms309.s1yn3.backend.service.RepositoryProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractEntityProviderService {
	@Autowired
	private RepositoryProviderService repositoryProviderService;

	protected RepositoryProviderService repositories() {
		return repositoryProviderService;
	}
}
