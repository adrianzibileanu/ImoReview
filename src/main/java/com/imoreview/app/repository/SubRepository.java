package com.imoreview.app.repository;

import com.imoreview.app.domain.Sub;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the Sub entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubRepository extends ReactiveMongoRepository<Sub, String> {
    Flux<Sub> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Sub> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Sub> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Sub> findOneWithEagerRelationships(String id);
}
