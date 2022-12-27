package com.imoreview.app.service;

import com.imoreview.app.service.dto.AttachmentDTO;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link com.imoreview.app.domain.Attachment}.
 */
public interface AttachmentService {
    /**
     * Save a attachment.
     *
     * @param attachmentDTO the entity to save.
     * @return the persisted entity.
     */
    Mono<AttachmentDTO> save(AttachmentDTO attachmentDTO);

    /**
     * Updates a attachment.
     *
     * @param attachmentDTO the entity to update.
     * @return the persisted entity.
     */
    Mono<AttachmentDTO> update(AttachmentDTO attachmentDTO);

    /**
     * Partially updates a attachment.
     *
     * @param attachmentDTO the entity to update partially.
     * @return the persisted entity.
     */
    Mono<AttachmentDTO> partialUpdate(AttachmentDTO attachmentDTO);

    /**
     * Get all the attachments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AttachmentDTO> findAll(Pageable pageable);

    /**
     * Get all the attachments with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Flux<AttachmentDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Returns the number of attachments available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" attachment.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<AttachmentDTO> findOne(String id);

    /**
     * Delete the "id" attachment.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(String id);
}
