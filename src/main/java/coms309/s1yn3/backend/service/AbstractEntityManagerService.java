package coms309.s1yn3.backend.service;

import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides a handle to repositories, and to entity providers with specialized query methods.
 */
@Service
public abstract class AbstractEntityManagerService {
	private static EntityProviderProviderService entityProviderProviderService;
	private static RepositoryProviderService repositoryProviderService;

	private static final Logger logger = LoggerFactory.logger(AbstractEntityManagerService.class);

	@Autowired
	public void setEntityProviderProviderService(EntityProviderProviderService entityProviderProviderService) {
		AbstractEntityManagerService.entityProviderProviderService = entityProviderProviderService;
	}

	@Autowired
	public void setRepositoryProviderService(RepositoryProviderService repositoryProviderService) {
		AbstractEntityManagerService.repositoryProviderService = repositoryProviderService;
	}

	protected static EntityProviderProviderService entityProviders() {
		return entityProviderProviderService;
	}

	protected static RepositoryProviderService repositories() {
		return repositoryProviderService;
	}
}
