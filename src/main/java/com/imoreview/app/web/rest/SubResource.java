package com.imoreview.app.web.rest;

import com.imoreview.app.domain.Sub;
import com.imoreview.app.repository.SubRepository;
import com.imoreview.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.imoreview.app.domain.Sub}.
 */
@RestController
@RequestMapping("/api")
public class SubResource {

    private final Logger log = LoggerFactory.getLogger(SubResource.class);

    private static final String ENTITY_NAME = "sub";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubRepository subRepository;

    public SubResource(SubRepository subRepository) {
        this.subRepository = subRepository;
    }

    /**
     * {@code POST  /subs} : Create a new sub.
     *
     * @param sub the sub to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sub, or with status {@code 400 (Bad Request)} if the sub has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/subs")
    public Mono<ResponseEntity<Sub>> createSub(@RequestBody Sub sub) throws URISyntaxException {
        log.debug("REST request to save Sub : {}", sub);
        if (sub.getId() != null) {
            throw new BadRequestAlertException("A new sub cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return subRepository
            .save(sub)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/subs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /subs/:id} : Updates an existing sub.
     *
     * @param id the id of the sub to save.
     * @param sub the sub to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sub,
     * or with status {@code 400 (Bad Request)} if the sub is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sub couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/subs/{id}")
    public Mono<ResponseEntity<Sub>> updateSub(@PathVariable(value = "id", required = false) final String id, @RequestBody Sub sub)
        throws URISyntaxException {
        log.debug("REST request to update Sub : {}, {}", id, sub);
        if (sub.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sub.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return subRepository
                    .save(sub)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /subs/:id} : Partial updates given fields of an existing sub, field will ignore if it is null
     *
     * @param id the id of the sub to save.
     * @param sub the sub to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sub,
     * or with status {@code 400 (Bad Request)} if the sub is not valid,
     * or with status {@code 404 (Not Found)} if the sub is not found,
     * or with status {@code 500 (Internal Server Error)} if the sub couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/subs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Sub>> partialUpdateSub(@PathVariable(value = "id", required = false) final String id, @RequestBody Sub sub)
        throws URISyntaxException {
        log.debug("REST request to partial update Sub partially : {}, {}", id, sub);
        if (sub.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sub.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return subRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Sub> result = subRepository
                    .findById(sub.getId())
                    .map(existingSub -> {
                        if (sub.getSubscribed() != null) {
                            existingSub.setSubscribed(sub.getSubscribed());
                        }
                        if (sub.getActive() != null) {
                            existingSub.setActive(sub.getActive());
                        }
                        if (sub.getExpirationDate() != null) {
                            existingSub.setExpirationDate(sub.getExpirationDate());
                        }
                        if (sub.getType() != null) {
                            existingSub.setType(sub.getType());
                        }

                        return existingSub;
                    })
                    .flatMap(subRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /subs} : get all the subs.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subs in body.
     */
    @GetMapping("/subs")
    public Mono<ResponseEntity<List<Sub>>> getAllSubs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Subs");
        return subRepository
            .count()
            .zipWith(subRepository.findAllBy(pageable).collectList())
            .map(countWithEntities ->
                ResponseEntity
                    .ok()
                    .headers(
                        PaginationUtil.generatePaginationHttpHeaders(
                            UriComponentsBuilder.fromHttpRequest(request),
                            new PageImpl<>(countWithEntities.getT2(), pageable, countWithEntities.getT1())
                        )
                    )
                    .body(countWithEntities.getT2())
            );
    }

    /**
     * {@code GET  /subs/:id} : get the "id" sub.
     *
     * @param id the id of the sub to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sub, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/subs/{id}")
    public Mono<ResponseEntity<Sub>> getSub(@PathVariable String id) {
        log.debug("REST request to get Sub : {}", id);
        Mono<Sub> sub = subRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(sub);
    }

    /**
     * {@code DELETE  /subs/:id} : delete the "id" sub.
     *
     * @param id the id of the sub to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/subs/{id}")
    public Mono<ResponseEntity<Void>> deleteSub(@PathVariable String id) {
        log.debug("REST request to delete Sub : {}", id);
        return subRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
