package org.springframework.data.jpa.repository.support;

import static org.springframework.data.querydsl.QueryDslUtils.QUERY_DSL_PRESENT;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy;
import org.springframework.data.jpa.repository.query.QueryExtractor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.util.Assert;

public class CustomJpaRepositoryFactory extends RepositoryFactorySupport {

	private final EntityManager entityManager;
	private final QueryExtractor extractor;
	private final CrudMethodMetadataPostProcessor lockModePostProcessor;

	public CustomJpaRepositoryFactory(EntityManager entityManager) {

		Assert.notNull(entityManager);

		this.entityManager = entityManager;
		this.extractor = PersistenceProvider.fromEntityManager(entityManager);
		this.lockModePostProcessor = CrudMethodMetadataPostProcessor.INSTANCE;

		addRepositoryProxyPostProcessor(lockModePostProcessor);
	}

	@Override
	protected Object getTargetRepository(RepositoryMetadata metadata) {

		CustomSimpleJpaRepository<?, ?> repository = getTargetRepository(metadata, entityManager);
		repository.setRepositoryMethodMetadata(lockModePostProcessor.getLockMetadataProvider());
		return repository;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <T, ID extends Serializable> CustomSimpleJpaRepository<?, ?> getTargetRepository(RepositoryMetadata metadata, EntityManager entityManager) {

		Class<?> repositoryInterface = metadata.getRepositoryInterface();
		JpaEntityInformation<?, Serializable> entityInformation = getEntityInformation(metadata.getDomainType());

		CustomSimpleJpaRepository<?, ?> repo = isQueryDslExecutor(repositoryInterface) ? new CustomQueryDslJpaRepository(entityInformation, entityManager) : new CustomSimpleJpaRepository(entityInformation, entityManager);

		return repo;
	}

	@Override
	protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

		if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
			return CustomQueryDslJpaRepository.class;
		} else {
			return CustomSimpleJpaRepository.class;
		}
	}

	private boolean isQueryDslExecutor(Class<?> repositoryInterface) {

		return QUERY_DSL_PRESENT && QueryDslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
	}

	@Override
	protected QueryLookupStrategy getQueryLookupStrategy(Key key) {

		return JpaQueryLookupStrategy.create(entityManager, key, extractor);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> JpaEntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {

		return (JpaEntityInformation<T, ID>) JpaEntityInformationSupport.getMetadata(domainClass, entityManager);
	}
}
