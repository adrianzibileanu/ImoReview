package com.imoreview.app.web.rest;

import com.imoreview.app.domain.Attachment;
import com.imoreview.app.repository.AttachmentRepository;
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
 * REST controller for managing {@link com.imoreview.app.domain.Attachment}.
 */
@RestController
@RequestMapping("/api")
public class AttachmentResource {

    private final Logger log = LoggerFactory.getLogger(AttachmentResource.class);

    private static final String ENTITY_NAME = "attachment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AttachmentRepository attachmentRepository;

    public AttachmentResource(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    /**
     * {@code POST  /attachments} : Create a new attachment.
     *
     * @param attachment the attachment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new attachment, or with status {@code 400 (Bad Request)} if the attachment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/attachments")
    public Mono<ResponseEntity<Attachment>> createAttachment(@Valid @RequestBody Attachment attachment) throws URISyntaxException {
        log.debug("REST request to save Attachment : {}", attachment);
        if (attachment.getId() != null) {
            throw new BadRequestAlertException("A new attachment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return attachmentRepository
            .save(attachment)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/attachments/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /attachments/:id} : Updates an existing attachment.
     *
     * @param id the id of the attachment to save.
     * @param attachment the attachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachment,
     * or with status {@code 400 (Bad Request)} if the attachment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the attachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/attachments/{id}")
    public Mono<ResponseEntity<Attachment>> updateAttachment(
        @PathVariable(value = "id", required = false) final String id,
        @Valid @RequestBody Attachment attachment
    ) throws URISyntaxException {
        log.debug("REST request to update Attachment : {}, {}", id, attachment);
        if (attachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return attachmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return attachmentRepository
                    .save(attachment)
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
     * {@code PATCH  /attachments/:id} : Partial updates given fields of an existing attachment, field will ignore if it is null
     *
     * @param id the id of the attachment to save.
     * @param attachment the attachment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated attachment,
     * or with status {@code 400 (Bad Request)} if the attachment is not valid,
     * or with status {@code 404 (Not Found)} if the attachment is not found,
     * or with status {@code 500 (Internal Server Error)} if the attachment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/attachments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<Attachment>> partialUpdateAttachment(
        @PathVariable(value = "id", required = false) final String id,
        @NotNull @RequestBody Attachment attachment
    ) throws URISyntaxException {
        log.debug("REST request to partial update Attachment partially : {}, {}", id, attachment);
        if (attachment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, attachment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return attachmentRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<Attachment> result = attachmentRepository
                    .findById(attachment.getId())
                    .map(existingAttachment -> {
                        if (attachment.getFileName() != null) {
                            existingAttachment.setFileName(attachment.getFileName());
                        }
                        if (attachment.getOriginalFileName() != null) {
                            existingAttachment.setOriginalFileName(attachment.getOriginalFileName());
                        }
                        if (attachment.getExtension() != null) {
                            existingAttachment.setExtension(attachment.getExtension());
                        }
                        if (attachment.getSizeInBytes() != null) {
                            existingAttachment.setSizeInBytes(attachment.getSizeInBytes());
                        }
                        if (attachment.getUploadedDate() != null) {
                            existingAttachment.setUploadedDate(attachment.getUploadedDate());
                        }
                        if (attachment.getSha256() != null) {
                            existingAttachment.setSha256(attachment.getSha256());
                        }
                        if (attachment.getContentType() != null) {
                            existingAttachment.setContentType(attachment.getContentType());
                        }

                        return existingAttachment;
                    })
                    .flatMap(attachmentRepository::save);

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
     * {@code GET  /attachments} : get all the attachments.
     *
     * @param pageable the pagination information.
     * @param request a {@link ServerHttpRequest} request.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of attachments in body.
     */
    @GetMapping("/attachments")
    public Mono<ResponseEntity<List<Attachment>>> getAllAttachments(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        ServerHttpRequest request,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Attachments");
        return attachmentRepository
            .count()
            .zipWith(attachmentRepository.findAllBy(pageable).collectList())
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
     * {@code GET  /attachments/:id} : get the "id" attachment.
     *
     * @param id the id of the attachment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the attachment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/attachments/{id}")
    public Mono<ResponseEntity<Attachment>> getAttachment(@PathVariable String id) {
        log.debug("REST request to get Attachment : {}", id);
        Mono<Attachment> attachment = attachmentRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(attachment);
    }

    /**
     * {@code DELETE  /attachments/:id} : delete the "id" attachment.
     *
     * @param id the id of the attachment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/attachments/{id}")
    public Mono<ResponseEntity<Void>> deleteAttachment(@PathVariable String id) {
        log.debug("REST request to delete Attachment : {}", id);
        return attachmentRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
                )
            );
    }
}
