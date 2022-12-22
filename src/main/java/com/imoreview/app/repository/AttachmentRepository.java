package com.imoreview.app.repository;

import com.imoreview.app.domain.Attachment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data MongoDB reactive repository for the Attachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AttachmentRepository extends ReactiveMongoRepository<Attachment, String> {
    Flux<Attachment> findAllBy(Pageable pageable);

    @Query("{}")
    Flux<Attachment> findAllWithEagerRelationships(Pageable pageable);

    @Query("{}")
    Flux<Attachment> findAllWithEagerRelationships();

    @Query("{'id': ?0}")
    Mono<Attachment> findOneWithEagerRelationships(String id);
}
