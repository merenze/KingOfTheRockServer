package coms309.s1yn3.backend.service.entityprovider;

import coms309.s1yn3.backend.service.RepositoryProviderService;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides a handle to repositories, and to entity providers with specialized query methods.
 */
@Service
public class AbstractEntityManagerService {
	private static EntityProviderService entityProviderService;
	private static RepositoryProviderService repositoryProviderService;

	private static final Logger logger = LoggerFactory.logger(AbstractEntityManagerService.class);

	@Autowired
	public void setEntityProviderService(EntityProviderService entityProviderService) {
		logger.debugf("Injecting EntityProviderService");
		AbstractEntityManagerService.entityProviderService = entityProviderService;
	}

	@Autowired
	public void setRepositoryProviderService(RepositoryProviderService repositoryProviderService) {
		logger.debugf("Injecting RepositoryProviderService");
		AbstractEntityManagerService.repositoryProviderService = repositoryProviderService;
	}

	protected static EntityProviderService entityProviders() {
		return entityProviderService;
	}

	protected static RepositoryProviderService repositories() {
		return repositoryProviderService;
	}
}
