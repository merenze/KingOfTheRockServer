package coms309.s1yn3.backend.service.entityprovider;

import coms309.s1yn3.backend.service.EntityProviderProviderService;
import coms309.s1yn3.backend.service.RepositoryProviderService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides a handle to repositories, and to entity providers with specialized query methods.
 */
@Service
public abstract class AbstractEntityProviderService {
	private static RepositoryProviderService repositoryProviderService;

	private static final Logger logger = LoggerFactory.logger(AbstractEntityProviderService.class);

	@Autowired
	public void setRepositoryProviderService(RepositoryProviderService repositoryProviderService) {
		AbstractEntityProviderService.repositoryProviderService = repositoryProviderService;
	}

	protected static RepositoryProviderService repositories() {
		return repositoryProviderService;
	}
}
