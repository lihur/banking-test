package com.trialwork.swedbank.banking.config.database;

import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Optional;

@NoRepositoryBean
public interface AppRepository<T, I extends Serializable> extends Repository<T, I> {

    <U> Optional<U> findOne(final Specification<T> spec, final SingularAttribute<T, U> attribute);
}