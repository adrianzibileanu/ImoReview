package com.imoreview.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.imoreview.app.IntegrationTest;
import com.imoreview.app.domain.Attachment;
import com.imoreview.app.repository.AttachmentRepository;
import com.imoreview.app.service.AttachmentService;
import com.imoreview.app.service.dto.AttachmentDTO;
import com.imoreview.app.service.mapper.AttachmentMapper;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link AttachmentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class AttachmentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_CV_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_CV_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_CV_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_CV_FILE_CONTENT_TYPE = "image/png";

    //  private static final String DEFAULT_CV_FILE_CONTENT_TYPE = "AAAAAAAAAA";
    //  private static final String UPDATED_CV_FILE_CONTENT_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Mock
    private AttachmentRepository attachmentRepositoryMock;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Mock
    private AttachmentService attachmentServiceMock;

    @Autowired
    private WebTestClient webTestClient;

    private Attachment attachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachment createEntity() {
        Attachment attachment = new Attachment()
            .name(DEFAULT_NAME)
            .cvFile(DEFAULT_CV_FILE)
            .cvFileContentType(DEFAULT_CV_FILE_CONTENT_TYPE)
            .cvFileContentType(DEFAULT_CV_FILE_CONTENT_TYPE);
        return attachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Attachment createUpdatedEntity() {
        Attachment attachment = new Attachment()
            .name(UPDATED_NAME)
            .cvFile(UPDATED_CV_FILE)
            .cvFileContentType(UPDATED_CV_FILE_CONTENT_TYPE)
            .cvFileContentType(UPDATED_CV_FILE_CONTENT_TYPE);
        return attachment;
    }

    @BeforeEach
    public void initTest() {
        attachmentRepository.deleteAll().block();
        attachment = createEntity();
    }

    @Test
    void createAttachment() throws Exception {
        int databaseSizeBeforeCreate = attachmentRepository.findAll().collectList().block().size();
        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate + 1);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttachment.getCvFile()).isEqualTo(DEFAULT_CV_FILE);
        assertThat(testAttachment.getCvFileContentType()).isEqualTo(DEFAULT_CV_FILE_CONTENT_TYPE);
        assertThat(testAttachment.getCvFileContentType()).isEqualTo(DEFAULT_CV_FILE_CONTENT_TYPE);
    }

    @Test
    void createAttachmentWithExistingId() throws Exception {
        // Create the Attachment with an existing ID
        attachment.setId("existing_id");
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        int databaseSizeBeforeCreate = attachmentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().collectList().block().size();
        // set the field null
        attachment.setName(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCvFileContentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().collectList().block().size();
        // set the field null
        attachment.setCvFileContentType(null);

        // Create the Attachment, which fails.
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAttachments() {
        // Initialize the database
        attachmentRepository.save(attachment).block();

        // Get all the attachmentList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(attachment.getId()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].cvFileContentType")
            .value(hasItem(DEFAULT_CV_FILE_CONTENT_TYPE))
            .jsonPath("$.[*].cvFile")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_CV_FILE)))
            .jsonPath("$.[*].cvFileContentType")
            .value(hasItem(DEFAULT_CV_FILE_CONTENT_TYPE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttachmentsWithEagerRelationshipsIsEnabled() {
        when(attachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(attachmentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttachmentsWithEagerRelationshipsIsNotEnabled() {
        when(attachmentServiceMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(attachmentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getAttachment() {
        // Initialize the database
        attachmentRepository.save(attachment).block();

        // Get the attachment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, attachment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(attachment.getId()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.cvFileContentType")
            .value(is(DEFAULT_CV_FILE_CONTENT_TYPE))
            .jsonPath("$.cvFile")
            .value(is(Base64Utils.encodeToString(DEFAULT_CV_FILE)))
            .jsonPath("$.cvFileContentType")
            .value(is(DEFAULT_CV_FILE_CONTENT_TYPE));
    }

    @Test
    void getNonExistingAttachment() {
        // Get the attachment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingAttachment() throws Exception {
        // Initialize the database
        attachmentRepository.save(attachment).block();

        int databaseSizeBeforeUpdate = attachmentRepository.findAll().collectList().block().size();

        // Update the attachment
        Attachment updatedAttachment = attachmentRepository.findById(attachment.getId()).block();
        updatedAttachment
            .name(UPDATED_NAME)
            .cvFile(UPDATED_CV_FILE)
            .cvFileContentType(UPDATED_CV_FILE_CONTENT_TYPE)
            .cvFileContentType(UPDATED_CV_FILE_CONTENT_TYPE);
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(updatedAttachment);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, attachmentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttachment.getCvFile()).isEqualTo(UPDATED_CV_FILE);
        assertThat(testAttachment.getCvFileContentType()).isEqualTo(UPDATED_CV_FILE_CONTENT_TYPE);
        assertThat(testAttachment.getCvFileContentType()).isEqualTo(UPDATED_CV_FILE_CONTENT_TYPE);
    }

    @Test
    void putNonExistingAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().collectList().block().size();
        attachment.setId(UUID.randomUUID().toString());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, attachmentDTO.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().collectList().block().size();
        attachment.setId(UUID.randomUUID().toString());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().collectList().block().size();
        attachment.setId(UUID.randomUUID().toString());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAttachmentWithPatch() throws Exception {
        // Initialize the database
        attachmentRepository.save(attachment).block();

        int databaseSizeBeforeUpdate = attachmentRepository.findAll().collectList().block().size();

        // Update the attachment using partial update
        Attachment partialUpdatedAttachment = new Attachment();
        partialUpdatedAttachment.setId(attachment.getId());

        partialUpdatedAttachment.cvFile(UPDATED_CV_FILE).cvFileContentType(UPDATED_CV_FILE_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttachment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAttachment.getCvFile()).isEqualTo(UPDATED_CV_FILE);
        assertThat(testAttachment.getCvFileContentType()).isEqualTo(UPDATED_CV_FILE_CONTENT_TYPE);
        assertThat(testAttachment.getCvFileContentType()).isEqualTo(DEFAULT_CV_FILE_CONTENT_TYPE);
    }

    @Test
    void fullUpdateAttachmentWithPatch() throws Exception {
        // Initialize the database
        attachmentRepository.save(attachment).block();

        int databaseSizeBeforeUpdate = attachmentRepository.findAll().collectList().block().size();

        // Update the attachment using partial update
        Attachment partialUpdatedAttachment = new Attachment();
        partialUpdatedAttachment.setId(attachment.getId());

        partialUpdatedAttachment
            .name(UPDATED_NAME)
            .cvFile(UPDATED_CV_FILE)
            .cvFileContentType(UPDATED_CV_FILE_CONTENT_TYPE)
            .cvFileContentType(UPDATED_CV_FILE_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedAttachment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedAttachment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAttachment.getCvFile()).isEqualTo(UPDATED_CV_FILE);
        assertThat(testAttachment.getCvFileContentType()).isEqualTo(UPDATED_CV_FILE_CONTENT_TYPE);
        assertThat(testAttachment.getCvFileContentType()).isEqualTo(UPDATED_CV_FILE_CONTENT_TYPE);
    }

    @Test
    void patchNonExistingAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().collectList().block().size();
        attachment.setId(UUID.randomUUID().toString());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, attachmentDTO.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().collectList().block().size();
        attachment.setId(UUID.randomUUID().toString());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().collectList().block().size();
        attachment.setId(UUID.randomUUID().toString());

        // Create the Attachment
        AttachmentDTO attachmentDTO = attachmentMapper.toDto(attachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachmentDTO))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAttachment() {
        // Initialize the database
        attachmentRepository.save(attachment).block();

        int databaseSizeBeforeDelete = attachmentRepository.findAll().collectList().block().size();

        // Delete the attachment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, attachment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
