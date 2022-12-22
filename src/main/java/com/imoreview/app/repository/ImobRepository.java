package com.imoreview.app.repository;

import com.imoreview.app.domain.Imob;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the Imob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImobRepository extends ReactiveMongoRepository<Imob, String> {
    Flux<Imob> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Imob> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Imob> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Imob> findOneWithEagerRelationships(String id);
}
