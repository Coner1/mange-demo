package org.springframework.data.jpa.repository.support;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.PathBuilder;

public class CustomQueryDslJpaRepository<T, ID extends Serializable> extends CustomSimpleJpaRepository<T, ID> implements QueryDslPredicateExecutor<T> {

    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;

    private final EntityPath<T> path;
    private final PathBuilder<T> builder;
    private final Querydsl querydsl;

    public CustomQueryDslJpaRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        this(entityInformation, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
    }

    public CustomQueryDslJpaRepository(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager, EntityPathResolver resolver) {

        super(entityInformation, entityManager);

        this.path = resolver.createPath(entityInformation.getJavaType());
        this.builder = new PathBuilder<T>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }

    public T findOne(Predicate predicate) {
        return createQuery(predicate).uniqueResult(path);
    }

    public List<T> findAll(Predicate predicate) {
        return createQuery(predicate).list(path);
    }

    public List<T> findAll(Predicate predicate, OrderSpecifier<?>... orders) {

        JPQLQuery query = createQuery(predicate);
        query = querydsl.applySorting(new QSort(orders), query);
        return query.list(path);
    }

    public Page<T> findAll(Predicate predicate, Pageable pageable) {

        JPQLQuery countQuery = createQuery(predicate);
        JPQLQuery query = querydsl.applyPagination(pageable, createQuery(predicate));

        Long total = countQuery.count();
        List<T> content = total > pageable.getOffset() ? query.list(path) : Collections.<T>emptyList();

        return new PageImpl<T>(content, pageable, total);
    }

    public long count(Predicate predicate) {
        return createQuery(predicate).count();
    }

    protected JPQLQuery createQuery(Predicate... predicate) {
        return querydsl.createQuery(path).where(predicate);
    }

    @Override
    public boolean exists(Predicate arg0) {
        return false;
    }

    @Override
    public Iterable<T> findAll(OrderSpecifier<?>... arg0) {
        return null;
    }

    @Override
    public Iterable<T> findAll(Predicate arg0, Sort arg1) {
        return null;
    }
}
