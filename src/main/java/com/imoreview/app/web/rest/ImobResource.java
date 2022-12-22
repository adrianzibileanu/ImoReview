package com.imoreview.app.web.rest;

import com.imoreview.app.domain.Imob;
import com.imoreview.app.repository.ImobRepository;
import com.imoreview.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.imoreview.app.domain.Imob}.
 */
@RestController
@RequestMapping("/api")
public class ImobResource {

    private final Logger log = LoggerFactory.getLogger(ImobResource.class);

    private static final String ENTITY_NAME = "imob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImobRepository imobRepository;

    public ImobResource(ImobRepository imobRepository) {
        this.imobRepository = imobRepository;
    }

    /**
     * {@code POST  /imobs} : Create a new imob.
     *
     * @param imob the imob to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imob, or with status {@code 400 (Bad Request)} if the imob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/imobs")
    public Mono<ResponseEntity<Imob>> createImob(@Valid @RequestBody Imob imob) throws URISyntaxException {
        log.debug("REST request to save Imob : {}", imob);
        if (imob.getId() != null) {
            throw new BadRequestAlertException("A new imob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return imobRepository
            .save(imob)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/imobs/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /imobs/:id} : Updates an existing imob.
     *
     * @param id the id of the imob to save.
     * @param imob the imob to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imob,
     * or with status {@code 400 (Bad Request)} if the imob is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imob couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/imobs/{id}")
    public Mono<ResponseEntity<Imob>> updateImob(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Imob imob
    ) throws URISyntaxException {
        log.debug("REST request to update Imob : {}, {}", id, imob);
        if (imob.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imob.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return imobRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return imobRepository
                    .save(imob)
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
     * {@code PATCH  /imobs/:id} : Partial updates given fields of an existing imob, field will ignore if it is null
     *
     * @param id the id of the imob to save.
     * @param imob the imob to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imob,
     * or with status {@code 400 (Bad Request)} if the imob is not valid,
     * or with status {@code 404 (Not Found)} if the imob is not found,
     * or with status {@code 500 (Internal Server Error)} if the imob couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/imobs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Imob>> partialUpdateImob(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Imob imob
    ) throws URISyntaxException {
        log.debug("REST request to partial update Imob partially : {}, {}", id, imob);
        if (imob.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imob.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return imobRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Imob> result = imobRepository
                    .findById(imob.getId())
                    .map(existingImob -> {
                        if (imob.getTitle() != null) {
                            existingImob.setTitle(imob.getTitle());
                        }
                        if (imob.getDescription() != null) {
                            existingImob.setDescription(imob.getDescription());
                        }
                        if (imob.getType() != null) {
                            existingImob.setType(imob.getType());
                        }
                        if (imob.getCateg() != null) {
                            existingImob.setCateg(imob.getCateg());
                        }
                        if (imob.getService() != null) {
                            existingImob.setService(imob.getService());
                        }
                        if (imob.getPrice() != null) {
                            existingImob.setPrice(imob.getPrice());
                        }
                        if (imob.getPriceCurrency() != null) {
                            existingImob.setPriceCurrency(imob.getPriceCurrency());
                        }
                        if (imob.getTags() != null) {
                            existingImob.setTags(imob.getTags());
                        }
                        if (imob.getAddress() != null) {
                            existingImob.setAddress(imob.getAddress());
                        }
                        if (imob.getContact() != null) {
                            existingImob.setContact(imob.getContact());
                        }
                        if (imob.getNbofRooms() != null) {
                            existingImob.setNbofRooms(imob.getNbofRooms());
                        }
                        if (imob.getConstrYear() != null) {
                            existingImob.setConstrYear(imob.getConstrYear());
                        }
                        if (imob.getUseSurface() != null) {
                            existingImob.setUseSurface(imob.getUseSurface());
                        }
                        if (imob.getBuiltSurface() != null) {
                            existingImob.setBuiltSurface(imob.getBuiltSurface());
                        }
                        if (imob.getCompart() != null) {
                            existingImob.setCompart(imob.getCompart());
                        }
                        if (imob.getConfort() != null) {
                            existingImob.setConfort(imob.getConfort());
                        }
                        if (imob.getFloor() != null) {
                            existingImob.setFloor(imob.getFloor());
                        }
                        if (imob.getNbofKitchens() != null) {
                            existingImob.setNbofKitchens(imob.getNbofKitchens());
                        }
                        if (imob.getNbofBthrooms() != null) {
                            existingImob.setNbofBthrooms(imob.getNbofBthrooms());
                        }
                        if (imob.getUnitType() != null) {
                            existingImob.setUnitType(imob.getUnitType());
                        }
                        if (imob.getUnitHeight() != null) {
                            existingImob.setUnitHeight(imob.getUnitHeight());
                        }
                        if (imob.getNbofBalconies() != null) {
                            existingImob.setNbofBalconies(imob.getNbofBalconies());
                        }
                        if (imob.getUtilities() != null) {
                            existingImob.setUtilities(imob.getUtilities());
                        }
                        if (imob.getFeatures() != null) {
                            existingImob.setFeatures(imob.getFeatures());
                        }
                        if (imob.getOtherdetails() != null) {
                            existingImob.setOtherdetails(imob.getOtherdetails());
                        }
                        if (imob.getZoneDetails() != null) {
                            existingImob.setZoneDetails(imob.getZoneDetails());
                        }
                        if (imob.getAvailability() != null) {
                            existingImob.setAvailability(imob.getAvailability());
                        }
                        /*   if (imob.getOwnerid() != null) {
                            existingImob.setOwnerid(imob.getOwnerid());
                        }*/

                        return existingImob;
                    })
                    .flatMap(imobRepository::save);

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
     * {@code GET  /imobs} : get all the imobs.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imobs in body.
     */
    @GetMapping("/imobs")
    public Mono<ResponseEntity<List<Imob>>> getAllImobs(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Imobs");
        return imobRepository
            .count()
            .zipWith(imobRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /imobs/:id} : get the "id" imob.
     *
     * @param id the id of the imob to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imob, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/imobs/{id}")
    public Mono<ResponseEntity<Imob>> getImob(@PathVariable String id) {
        log.debug("REST request to get Imob : {}", id);
        Mono<Imob> imob = imobRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(imob);
    }

    /**
     * {@code DELETE  /imobs/:id} : delete the "id" imob.
     *
     * @param id the id of the imob to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/imobs/{id}")
    public Mono<ResponseEntity<Void>> deleteImob(@PathVariable String id) {
        log.debug("REST request to delete Imob : {}", id);
        return imobRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
