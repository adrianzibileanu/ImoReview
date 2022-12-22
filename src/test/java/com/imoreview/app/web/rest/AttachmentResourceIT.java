package com.imoreview.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.imoreview.app.IntegrationTest;
import com.imoreview.app.domain.Attachment;
import com.imoreview.app.repository.AttachmentRepository;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EXTENSION = "AAAAAAAAAA";
    private static final String UPDATED_EXTENSION = "BBBBBBBBBB";

    private static final Integer DEFAULT_SIZE_IN_BYTES = 1;
    private static final Integer UPDATED_SIZE_IN_BYTES = 2;

    private static final Instant DEFAULT_UPLOADED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOADED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Mock
    private AttachmentRepository attachmentRepositoryMock;

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
            .fileName(DEFAULT_FILE_NAME)
            .originalFileName(DEFAULT_ORIGINAL_FILE_NAME)
            .extension(DEFAULT_EXTENSION)
            .sizeInBytes(DEFAULT_SIZE_IN_BYTES)
            .uploadedDate(DEFAULT_UPLOADED_DATE)
            .sha256(DEFAULT_SHA_256)
            .contentType(DEFAULT_CONTENT_TYPE);
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
            .fileName(UPDATED_FILE_NAME)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .extension(UPDATED_EXTENSION)
            .sizeInBytes(UPDATED_SIZE_IN_BYTES)
            .uploadedDate(UPDATED_UPLOADED_DATE)
            .sha256(UPDATED_SHA_256)
            .contentType(UPDATED_CONTENT_TYPE);
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
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate + 1);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testAttachment.getOriginalFileName()).isEqualTo(DEFAULT_ORIGINAL_FILE_NAME);
        assertThat(testAttachment.getExtension()).isEqualTo(DEFAULT_EXTENSION);
        assertThat(testAttachment.getSizeInBytes()).isEqualTo(DEFAULT_SIZE_IN_BYTES);
        assertThat(testAttachment.getUploadedDate()).isEqualTo(DEFAULT_UPLOADED_DATE);
        assertThat(testAttachment.getSha256()).isEqualTo(DEFAULT_SHA_256);
        assertThat(testAttachment.getContentType()).isEqualTo(DEFAULT_CONTENT_TYPE);
    }

    @Test
    void createAttachmentWithExistingId() throws Exception {
        // Create the Attachment with an existing ID
        attachment.setId("existing_id");

        int databaseSizeBeforeCreate = attachmentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().collectList().block().size();
        // set the field null
        attachment.setFileName(null);

        // Create the Attachment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkOriginalFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().collectList().block().size();
        // set the field null
        attachment.setOriginalFileName(null);

        // Create the Attachment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkExtensionIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().collectList().block().size();
        // set the field null
        attachment.setExtension(null);

        // Create the Attachment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSizeInBytesIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().collectList().block().size();
        // set the field null
        attachment.setSizeInBytes(null);

        // Create the Attachment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUploadedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().collectList().block().size();
        // set the field null
        attachment.setUploadedDate(null);

        // Create the Attachment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSha256IsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().collectList().block().size();
        // set the field null
        attachment.setSha256(null);

        // Create the Attachment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkContentTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = attachmentRepository.findAll().collectList().block().size();
        // set the field null
        attachment.setContentType(null);

        // Create the Attachment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
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
            .jsonPath("$.[*].fileName")
            .value(hasItem(DEFAULT_FILE_NAME))
            .jsonPath("$.[*].originalFileName")
            .value(hasItem(DEFAULT_ORIGINAL_FILE_NAME))
            .jsonPath("$.[*].extension")
            .value(hasItem(DEFAULT_EXTENSION))
            .jsonPath("$.[*].sizeInBytes")
            .value(hasItem(DEFAULT_SIZE_IN_BYTES))
            .jsonPath("$.[*].uploadedDate")
            .value(hasItem(DEFAULT_UPLOADED_DATE.toString()))
            .jsonPath("$.[*].sha256")
            .value(hasItem(DEFAULT_SHA_256))
            .jsonPath("$.[*].contentType")
            .value(hasItem(DEFAULT_CONTENT_TYPE));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttachmentsWithEagerRelationshipsIsEnabled() {
        when(attachmentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(attachmentRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAttachmentsWithEagerRelationshipsIsNotEnabled() {
        when(attachmentRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

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
            .jsonPath("$.fileName")
            .value(is(DEFAULT_FILE_NAME))
            .jsonPath("$.originalFileName")
            .value(is(DEFAULT_ORIGINAL_FILE_NAME))
            .jsonPath("$.extension")
            .value(is(DEFAULT_EXTENSION))
            .jsonPath("$.sizeInBytes")
            .value(is(DEFAULT_SIZE_IN_BYTES))
            .jsonPath("$.uploadedDate")
            .value(is(DEFAULT_UPLOADED_DATE.toString()))
            .jsonPath("$.sha256")
            .value(is(DEFAULT_SHA_256))
            .jsonPath("$.contentType")
            .value(is(DEFAULT_CONTENT_TYPE));
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
            .fileName(UPDATED_FILE_NAME)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .extension(UPDATED_EXTENSION)
            .sizeInBytes(UPDATED_SIZE_IN_BYTES)
            .uploadedDate(UPDATED_UPLOADED_DATE)
            .sha256(UPDATED_SHA_256)
            .contentType(UPDATED_CONTENT_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedAttachment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedAttachment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Attachment in the database
        List<Attachment> attachmentList = attachmentRepository.findAll().collectList().block();
        assertThat(attachmentList).hasSize(databaseSizeBeforeUpdate);
        Attachment testAttachment = attachmentList.get(attachmentList.size() - 1);
        assertThat(testAttachment.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testAttachment.getOriginalFileName()).isEqualTo(UPDATED_ORIGINAL_FILE_NAME);
        assertThat(testAttachment.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testAttachment.getSizeInBytes()).isEqualTo(UPDATED_SIZE_IN_BYTES);
        assertThat(testAttachment.getUploadedDate()).isEqualTo(UPDATED_UPLOADED_DATE);
        assertThat(testAttachment.getSha256()).isEqualTo(UPDATED_SHA_256);
        assertThat(testAttachment.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
    }

    @Test
    void putNonExistingAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().collectList().block().size();
        attachment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, attachment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
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

        partialUpdatedAttachment
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .sizeInBytes(UPDATED_SIZE_IN_BYTES)
            .contentType(UPDATED_CONTENT_TYPE);

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
        assertThat(testAttachment.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testAttachment.getOriginalFileName()).isEqualTo(UPDATED_ORIGINAL_FILE_NAME);
        assertThat(testAttachment.getExtension()).isEqualTo(DEFAULT_EXTENSION);
        assertThat(testAttachment.getSizeInBytes()).isEqualTo(UPDATED_SIZE_IN_BYTES);
        assertThat(testAttachment.getUploadedDate()).isEqualTo(DEFAULT_UPLOADED_DATE);
        assertThat(testAttachment.getSha256()).isEqualTo(DEFAULT_SHA_256);
        assertThat(testAttachment.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
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
            .fileName(UPDATED_FILE_NAME)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .extension(UPDATED_EXTENSION)
            .sizeInBytes(UPDATED_SIZE_IN_BYTES)
            .uploadedDate(UPDATED_UPLOADED_DATE)
            .sha256(UPDATED_SHA_256)
            .contentType(UPDATED_CONTENT_TYPE);

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
        assertThat(testAttachment.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testAttachment.getOriginalFileName()).isEqualTo(UPDATED_ORIGINAL_FILE_NAME);
        assertThat(testAttachment.getExtension()).isEqualTo(UPDATED_EXTENSION);
        assertThat(testAttachment.getSizeInBytes()).isEqualTo(UPDATED_SIZE_IN_BYTES);
        assertThat(testAttachment.getUploadedDate()).isEqualTo(UPDATED_UPLOADED_DATE);
        assertThat(testAttachment.getSha256()).isEqualTo(UPDATED_SHA_256);
        assertThat(testAttachment.getContentType()).isEqualTo(UPDATED_CONTENT_TYPE);
    }

    @Test
    void patchNonExistingAttachment() throws Exception {
        int databaseSizeBeforeUpdate = attachmentRepository.findAll().collectList().block().size();
        attachment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, attachment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(attachment))
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
