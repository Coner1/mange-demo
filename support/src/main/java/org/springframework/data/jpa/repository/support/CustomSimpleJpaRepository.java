package org.springframework.data.jpa.repository.support;

import static org.springframework.data.jpa.repository.query.QueryUtils.COUNT_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.DELETE_ALL_QUERY_STRING;
import static org.springframework.data.jpa.repository.query.QueryUtils.applyAndBind;
import static org.springframework.data.jpa.repository.query.QueryUtils.getQueryString;
import static org.springframework.data.jpa.repository.query.QueryUtils.toOrders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Repository
public class CustomSimpleJpaRepository<T, ID extends Serializable> implements JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

	private final JpaEntityInformation<T, ?> entityInformation;
	private final EntityManager em;
	private final PersistenceProvider provider;

	private CrudMethodMetadata crudMethodMetadata;

	public CustomSimpleJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {

		Assert.notNull(entityInformation);
		Assert.notNull(entityManager);

		this.entityInformation = entityInformation;
		this.em = entityManager;
		this.provider = PersistenceProvider.fromEntityManager(entityManager);
	}

	public CustomSimpleJpaRepository(Class<T> domainClass, EntityManager em) {
		this(JpaEntityInformationSupport.getMetadata(domainClass, em), em);
	}

	public void setRepositoryMethodMetadata(CrudMethodMetadata crudMethodMetadata) {
		this.crudMethodMetadata = crudMethodMetadata;
	}

	protected Class<T> getDomainClass() {
		return entityInformation.getJavaType();
	}

	private String getDeleteAllQueryString() {
		return getQueryString(DELETE_ALL_QUERY_STRING, entityInformation.getEntityName());
	}

	private String getCountQueryString() {

		String countQuery = String.format(COUNT_QUERY_STRING, provider.getCountQueryPlaceholder(), "%s");
		return getQueryString(countQuery, entityInformation.getEntityName());
	}

	@Transactional
	public void delete(ID id) {

		Assert.notNull(id, "The given id must not be null!");

		T entity = findOne(id);

		if (entity == null) {
			throw new EmptyResultDataAccessException(String.format("No %s entity with id %s exists!", entityInformation.getJavaType(), id), 1);
		}

		delete(entity);
	}

	@Transactional
	public void delete(T entity) {

		Assert.notNull(entity, "The entity must not be null!");
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}

	@Transactional
	public void delete(Iterable<? extends T> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		for (T entity : entities) {
			delete(entity);
		}
	}

	@Transactional
	public void deleteInBatch(Iterable<T> entities) {

		Assert.notNull(entities, "The given Iterable of entities not be null!");

		if (!entities.iterator().hasNext()) {
			return;
		}

		applyAndBind(getQueryString(DELETE_ALL_QUERY_STRING, entityInformation.getEntityName()), entities, em).executeUpdate();
	}

	@Transactional
	public void deleteAll() {

		for (T element : findAll()) {
			delete(element);
		}
	}

	@Transactional
	public void deleteAllInBatch() {
		em.createQuery(getDeleteAllQueryString()).executeUpdate();
	}

	public T findOne(ID id) {

		Assert.notNull(id, "The given id must not be null!");

		Class<T> domainType = getDomainClass();

		if (crudMethodMetadata == null) {
			return em.find(domainType, id);
		}

		LockModeType type = crudMethodMetadata.getLockModeType();
		Map<String, Object> hints = crudMethodMetadata.getQueryHints();

		return type == null ? em.find(domainType, id, hints) : em.find(domainType, id, type, hints);
	}

	@Override
	public T getOne(ID id) {

		Assert.notNull(id, "The given id must not be null!");
		return em.getReference(getDomainClass(), id);
	}

	public boolean exists(ID id) {

		Assert.notNull(id, "The given id must not be null!");

		if (entityInformation.getIdAttribute() != null) {

			String placeholder = provider.getCountQueryPlaceholder();
			String entityName = entityInformation.getEntityName();
			Iterable<String> idAttributeNames = entityInformation.getIdAttributeNames();
			String existsQuery = QueryUtils.getExistsQueryString(entityName, placeholder, idAttributeNames);

			TypedQuery<Long> query = em.createQuery(existsQuery, Long.class);

			if (entityInformation.hasCompositeId()) {
				for (String idAttributeName : idAttributeNames) {
					query.setParameter(idAttributeName, entityInformation.getCompositeIdAttributeValue(id, idAttributeName));
				}
			} else {
				query.setParameter(idAttributeNames.iterator().next(), id);
			}

			return query.getSingleResult() == 1L;
		} else {
			return findOne(id) != null;
		}
	}

	public List<T> findAll() {
		return getQuery(null, (Sort) null).getResultList();
	}

	public List<T> findAll(Iterable<ID> ids) {

		if (ids == null || !ids.iterator().hasNext()) {
			return Collections.emptyList();
		}

		ByIdsSpecification specification = new ByIdsSpecification();
		TypedQuery<T> query = getQuery(specification, (Sort) null);

		return query.setParameter(specification.parameter, ids).getResultList();
	}

	public List<T> findAll(Sort sort) {
		return getQuery(null, sort).getResultList();
	}

	public Page<T> findAll(Pageable pageable) {

		if (null == pageable) {
			return new PageImpl<T>(findAll());
		}

		return findAll(null, pageable);
	}

	public T findOne(Specification<T> spec) {

		try {
			return getQuery(spec, (Sort) null).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<T> findAll(Specification<T> spec) {
		return getQuery(spec, (Sort) null).getResultList();
	}

	public Page<T> findAll(Specification<T> spec, Pageable pageable) {

		TypedQuery<T> query = getQuery(spec, pageable);
		return pageable == null ? new PageImpl<T>(query.getResultList()) : readPage(query, pageable, spec);
	}

	public List<T> findAll(Specification<T> spec, Sort sort) {

		return getQuery(spec, sort).getResultList();
	}

	public long count() {
		return em.createQuery(getCountQueryString(), Long.class).getSingleResult();
	}

	public long count(Specification<T> spec) {

		return getCountQuery(spec).getSingleResult();
	}

	@Transactional
	public <S extends T> S save(S entity) {

		if (entityInformation.isNew(entity)) {
			em.persist(entity);
			return entity;
		} else {
			return em.merge(entity);
		}
	}

	@Transactional
	public <S extends T> S saveAndFlush(S entity) {

		S result = save(entity);
		flush();

		return result;
	}

	@Transactional
	public <S extends T> List<S> save(Iterable<S> entities) {

		List<S> result = new ArrayList<S>();

		if (entities == null) {
			return result;
		}

		for (S entity : entities) {
			result.add(save(entity));
		}

		return result;
	}

	@Transactional
	public void flush() {

		em.flush();
	}

	protected Page<T> readPage(TypedQuery<T> query, Pageable pageable, Specification<T> spec) {

		query.setFirstResult(pageable.getOffset());
		query.setMaxResults(pageable.getPageSize());

		Long total = QueryUtils.executeCountQuery(getCountQuery(spec));
		List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.<T> emptyList();

		return new PageImpl<T>(content, pageable, total);
	}

	protected TypedQuery<T> getQuery(Specification<T> spec, Pageable pageable) {

		Sort sort = pageable == null ? null : pageable.getSort();
		return getQuery(spec, sort);
	}

	protected TypedQuery<T> getQuery(Specification<T> spec, Sort sort) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(getDomainClass());

		Root<T> root = applySpecificationToCriteria(spec, query);
		query.select(root);

		if (sort != null) {
			query.orderBy(toOrders(sort, root, builder));
		}

		return applyRepositoryMethodMetadata(em.createQuery(query));
	}

	protected TypedQuery<Long> getCountQuery(Specification<T> spec) {

		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);

		Root<T> root = applySpecificationToCriteria(spec, query);

		if (query.isDistinct()) {
			query.select(builder.countDistinct(root));
		} else {
			query.select(builder.count(root));
		}

		return em.createQuery(query);
	}

	private <S> Root<T> applySpecificationToCriteria(Specification<T> spec, CriteriaQuery<S> query) {

		Assert.notNull(query);
		Root<T> root = query.from(getDomainClass());

		if (spec == null) {
			return root;
		}

		CriteriaBuilder builder = em.getCriteriaBuilder();
		Predicate predicate = spec.toPredicate(root, query, builder);

		if (predicate != null) {
			query.where(predicate);
		}

		return root;
	}

	private TypedQuery<T> applyRepositoryMethodMetadata(TypedQuery<T> query) {

		if (crudMethodMetadata == null) {
			return query;
		}

		LockModeType type = crudMethodMetadata.getLockModeType();
		TypedQuery<T> toReturn = type == null ? query : query.setLockMode(type);

		for (Entry<String, Object> hint : crudMethodMetadata.getQueryHints().entrySet()) {
			query.setHint(hint.getKey(), hint.getValue());
		}

		return toReturn;
	}

	@SuppressWarnings("rawtypes")
	private final class ByIdsSpecification implements Specification<T> {

		ParameterExpression<Iterable> parameter;

		public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

			Path<?> path = root.get(entityInformation.getIdAttribute());
			parameter = cb.parameter(Iterable.class);
			return path.in(parameter);
		}
	}
}
