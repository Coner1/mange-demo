package org.springframework.data.jpa.repository.support;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.util.Assert;

public class CustomJpaRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> {

	private EntityManager entityManager;

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public void setMappingContext(MappingContext<?, ?> mappingContext) {
		super.setMappingContext(mappingContext);
	}

	@Override
	protected RepositoryFactorySupport doCreateRepositoryFactory() {
		return createRepositoryFactory(entityManager);
	}

	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		return new CustomJpaRepositoryFactory(entityManager);
	}

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(entityManager, "EntityManager must not be null!");
		super.afterPropertiesSet();
	}
}
