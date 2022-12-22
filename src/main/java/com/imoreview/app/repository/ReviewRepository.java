package com.imoreview.app.repository;

import com.imoreview.app.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the Review entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReviewRepository extends ReactiveMongoRepository<Review, String> {
    Flux<Review> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Review> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Review> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Review> findOneWithEagerRelationships(String id);
}
