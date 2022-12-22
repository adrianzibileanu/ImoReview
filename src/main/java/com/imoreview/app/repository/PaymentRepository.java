package com.imoreview.app.repository;

import com.imoreview.app.domain.Payment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Spring Data MongoDB reactive repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends ReactiveMongoRepository<Payment, String> {
    Flux<Payment> findAllBy(Pageable pageable);
}
