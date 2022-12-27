package com.imoreview.app.service.impl;

import com.imoreview.app.domain.Attachment;
import com.imoreview.app.repository.AttachmentRepository;
import com.imoreview.app.service.AttachmentService;
import com.imoreview.app.service.dto.AttachmentDTO;
import com.imoreview.app.service.mapper.AttachmentMapper;
import javax.inject.Inject;
import javax.inject.Qualifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.quartz.JobStoreType;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Attachment}.
 */
@Service
@Transactional
public class AttachmentServiceImpl implements AttachmentService {

    private final Logger log = LoggerFactory.getLogger(AttachmentServiceImpl.class);

    private final AttachmentRepository attachmentRepository;

    private final AttachmentMapper attachmentMapper;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, AttachmentMapper attachmentMapper) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentMapper = attachmentMapper;
    }

    @Override
    public Mono<AttachmentDTO> save(AttachmentDTO attachmentDTO) {
        log.debug("Request to save Attachment : {}", attachmentDTO);
        return attachmentRepository.save(attachmentMapper.toEntity(attachmentDTO)).map(attachmentMapper::toDto);
    }

    @Override
    public Mono<AttachmentDTO> update(AttachmentDTO attachmentDTO) {
        log.debug("Request to update Attachment : {}", attachmentDTO);
        return attachmentRepository.save(attachmentMapper.toEntity(attachmentDTO)).map(attachmentMapper::toDto);
    }

    @Override
    public Mono<AttachmentDTO> partialUpdate(AttachmentDTO attachmentDTO) {
        log.debug("Request to partially update Attachment : {}", attachmentDTO);

        return attachmentRepository
            .findById(attachmentDTO.getId())
            .map(existingAttachment -> {
                attachmentMapper.partialUpdate(existingAttachment, attachmentDTO);

                return existingAttachment;
            })
            .flatMap(attachmentRepository::save)
            .map(attachmentMapper::toDto);
    }

    @Override
    public Flux<AttachmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Attachments");
        return attachmentRepository.findAllBy(pageable).map(attachmentMapper::toDto);
    }

    public Flux<AttachmentDTO> findAllWithEagerRelationships(Pageable pageable) {
        return attachmentRepository.findAllWithEagerRelationships(pageable).map(attachmentMapper::toDto);
    }

    public Mono<Long> countAll() {
        return attachmentRepository.count();
    }

    @Override
    public Mono<AttachmentDTO> findOne(String id) {
        log.debug("Request to get Attachment : {}", id);
        return attachmentRepository.findOneWithEagerRelationships(id).map(attachmentMapper::toDto);
    }

    @Override
    public Mono<Void> delete(String id) {
        log.debug("Request to delete Attachment : {}", id);
        return attachmentRepository.deleteById(id);
    }
}
