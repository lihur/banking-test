package com.trialwork.swedbank.banking.config.database;

import java.util.Optional;
import java.io.Serializable;
import jakarta.persistence.*;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.*;

public class AppRepositoryImpl<T, I extends Serializable> extends SimpleJpaRepository<T, I> implements AppRepository<T, I> {

    private final EntityManager entityManager;

    public AppRepositoryImpl(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public <U> Optional<U> findOne(final Specification<T> spec, final SingularAttribute<T, U> attribute) {
        final var cb = entityManager.getCriteriaBuilder();
        final var query = cb.createQuery(attribute.getJavaType());
        final var root = query.from(getDomainClass());
        query.select(root.get(attribute));
        query.where(spec.toPredicate(root, query, cb));
        try {
            return Optional.of(entityManager.createQuery(query).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}