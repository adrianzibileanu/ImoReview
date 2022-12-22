package com.imoreview.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.imoreview.app.IntegrationTest;
import com.imoreview.app.domain.Sub;
import com.imoreview.app.domain.enumeration.SubType;
import com.imoreview.app.repository.SubRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SubResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class SubResourceIT {

    private static final Boolean DEFAULT_SUBSCRIBED = false;
    private static final Boolean UPDATED_SUBSCRIBED = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final LocalDate DEFAULT_EXPIRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final SubType DEFAULT_TYPE = SubType.MTH;
    private static final SubType UPDATED_TYPE = SubType.ANN;

    private static final String ENTITY_API_URL = "/api/subs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private SubRepository subRepository;

    @Mock
    private SubRepository subRepositoryMock;

    @Autowired
    private WebTestClient webTestClient;

    private Sub sub;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sub createEntity() {
        Sub sub = new Sub()
            .subscribed(DEFAULT_SUBSCRIBED)
            .active(DEFAULT_ACTIVE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .type(DEFAULT_TYPE);
        return sub;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sub createUpdatedEntity() {
        Sub sub = new Sub()
            .subscribed(UPDATED_SUBSCRIBED)
            .active(UPDATED_ACTIVE)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .type(UPDATED_TYPE);
        return sub;
    }

    @BeforeEach
    public void initTest() {
        subRepository.deleteAll().block();
        sub = createEntity();
    }

    @Test
    void createSub() throws Exception {
        int databaseSizeBeforeCreate = subRepository.findAll().collectList().block().size();
        // Create the Sub
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sub))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Sub in the database
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeCreate + 1);
        Sub testSub = subList.get(subList.size() - 1);
        assertThat(testSub.getSubscribed()).isEqualTo(DEFAULT_SUBSCRIBED);
        assertThat(testSub.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testSub.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testSub.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createSubWithExistingId() throws Exception {
        // Create the Sub with an existing ID
        sub.setId("existing_id");

        int databaseSizeBeforeCreate = subRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sub))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sub in the database
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllSubs() {
        // Initialize the database
        subRepository.save(sub).block();

        // Get all the subList
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
            .value(hasItem(sub.getId()))
            .jsonPath("$.[*].subscribed")
            .value(hasItem(DEFAULT_SUBSCRIBED.booleanValue()))
            .jsonPath("$.[*].active")
            .value(hasItem(DEFAULT_ACTIVE.booleanValue()))
            .jsonPath("$.[*].expirationDate")
            .value(hasItem(DEFAULT_EXPIRATION_DATE.toString()))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubsWithEagerRelationshipsIsEnabled() {
        when(subRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(subRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSubsWithEagerRelationshipsIsNotEnabled() {
        when(subRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(subRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getSub() {
        // Initialize the database
        subRepository.save(sub).block();

        // Get the sub
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, sub.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(sub.getId()))
            .jsonPath("$.subscribed")
            .value(is(DEFAULT_SUBSCRIBED.booleanValue()))
            .jsonPath("$.active")
            .value(is(DEFAULT_ACTIVE.booleanValue()))
            .jsonPath("$.expirationDate")
            .value(is(DEFAULT_EXPIRATION_DATE.toString()))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()));
    }

    @Test
    void getNonExistingSub() {
        // Get the sub
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingSub() throws Exception {
        // Initialize the database
        subRepository.save(sub).block();

        int databaseSizeBeforeUpdate = subRepository.findAll().collectList().block().size();

        // Update the sub
        Sub updatedSub = subRepository.findById(sub.getId()).block();
        updatedSub.subscribed(UPDATED_SUBSCRIBED).active(UPDATED_ACTIVE).expirationDate(UPDATED_EXPIRATION_DATE).type(UPDATED_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedSub.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedSub))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sub in the database
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeUpdate);
        Sub testSub = subList.get(subList.size() - 1);
        assertThat(testSub.getSubscribed()).isEqualTo(UPDATED_SUBSCRIBED);
        assertThat(testSub.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testSub.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testSub.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingSub() throws Exception {
        int databaseSizeBeforeUpdate = subRepository.findAll().collectList().block().size();
        sub.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, sub.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sub))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sub in the database
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchSub() throws Exception {
        int databaseSizeBeforeUpdate = subRepository.findAll().collectList().block().size();
        sub.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sub))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sub in the database
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamSub() throws Exception {
        int databaseSizeBeforeUpdate = subRepository.findAll().collectList().block().size();
        sub.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(sub))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sub in the database
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateSubWithPatch() throws Exception {
        // Initialize the database
        subRepository.save(sub).block();

        int databaseSizeBeforeUpdate = subRepository.findAll().collectList().block().size();

        // Update the sub using partial update
        Sub partialUpdatedSub = new Sub();
        partialUpdatedSub.setId(sub.getId());

        partialUpdatedSub.active(UPDATED_ACTIVE).type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSub.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSub))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sub in the database
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeUpdate);
        Sub testSub = subList.get(subList.size() - 1);
        assertThat(testSub.getSubscribed()).isEqualTo(DEFAULT_SUBSCRIBED);
        assertThat(testSub.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testSub.getExpirationDate()).isEqualTo(DEFAULT_EXPIRATION_DATE);
        assertThat(testSub.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void fullUpdateSubWithPatch() throws Exception {
        // Initialize the database
        subRepository.save(sub).block();

        int databaseSizeBeforeUpdate = subRepository.findAll().collectList().block().size();

        // Update the sub using partial update
        Sub partialUpdatedSub = new Sub();
        partialUpdatedSub.setId(sub.getId());

        partialUpdatedSub.subscribed(UPDATED_SUBSCRIBED).active(UPDATED_ACTIVE).expirationDate(UPDATED_EXPIRATION_DATE).type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedSub.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedSub))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Sub in the database
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeUpdate);
        Sub testSub = subList.get(subList.size() - 1);
        assertThat(testSub.getSubscribed()).isEqualTo(UPDATED_SUBSCRIBED);
        assertThat(testSub.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testSub.getExpirationDate()).isEqualTo(UPDATED_EXPIRATION_DATE);
        assertThat(testSub.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingSub() throws Exception {
        int databaseSizeBeforeUpdate = subRepository.findAll().collectList().block().size();
        sub.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, sub.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sub))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sub in the database
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchSub() throws Exception {
        int databaseSizeBeforeUpdate = subRepository.findAll().collectList().block().size();
        sub.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sub))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Sub in the database
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamSub() throws Exception {
        int databaseSizeBeforeUpdate = subRepository.findAll().collectList().block().size();
        sub.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(sub))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Sub in the database
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteSub() {
        // Initialize the database
        subRepository.save(sub).block();

        int databaseSizeBeforeDelete = subRepository.findAll().collectList().block().size();

        // Delete the sub
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, sub.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Sub> subList = subRepository.findAll().collectList().block();
        assertThat(subList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
