package com.imoreview.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.imoreview.app.IntegrationTest;
import com.imoreview.app.domain.Review;
import com.imoreview.app.repository.ReviewRepository;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Integration tests for the {@link ReviewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ReviewResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final Integer DEFAULT_RATING = 1;
    private static final Integer UPDATED_RATING = 2;

    private static final String ENTITY_API_URL = "/api/reviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewRepository reviewRepositoryMock;

    @Autowired
    private WebTestClient webTestClient;

    private Review review;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createEntity() {
        Review review = new Review().title(DEFAULT_TITLE).body(DEFAULT_BODY).rating(DEFAULT_RATING);
        return review;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Review createUpdatedEntity() {
        Review review = new Review().title(UPDATED_TITLE).body(UPDATED_BODY).rating(UPDATED_RATING);
        return review;
    }

    @BeforeEach
    public void initTest() {
        reviewRepository.deleteAll().block();
        review = createEntity();
    }

    @Test
    void createReview() throws Exception {
        int databaseSizeBeforeCreate = reviewRepository.findAll().collectList().block().size();
        // Create the Review
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(review))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate + 1);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testReview.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testReview.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    void createReviewWithExistingId() throws Exception {
        // Create the Review with an existing ID
        review.setId("existing_id");

        int databaseSizeBeforeCreate = reviewRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(review))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().collectList().block().size();
        // set the field null
        review.setTitle(null);

        // Create the Review, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(review))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().collectList().block().size();
        // set the field null
        review.setBody(null);

        // Create the Review, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(review))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = reviewRepository.findAll().collectList().block().size();
        // set the field null
        review.setRating(null);

        // Create the Review, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(review))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllReviews() {
        // Initialize the database
        reviewRepository.save(review).block();

        // Get all the reviewList
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
            .value(hasItem(review.getId()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].body")
            .value(hasItem(DEFAULT_BODY))
            .jsonPath("$.[*].rating")
            .value(hasItem(DEFAULT_RATING));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewsWithEagerRelationshipsIsEnabled() {
        when(reviewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(reviewRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllReviewsWithEagerRelationshipsIsNotEnabled() {
        when(reviewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(reviewRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getReview() {
        // Initialize the database
        reviewRepository.save(review).block();

        // Get the review
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, review.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(review.getId()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.body")
            .value(is(DEFAULT_BODY))
            .jsonPath("$.rating")
            .value(is(DEFAULT_RATING));
    }

    @Test
    void getNonExistingReview() {
        // Get the review
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingReview() throws Exception {
        // Initialize the database
        reviewRepository.save(review).block();

        int databaseSizeBeforeUpdate = reviewRepository.findAll().collectList().block().size();

        // Update the review
        Review updatedReview = reviewRepository.findById(review.getId()).block();
        updatedReview.title(UPDATED_TITLE).body(UPDATED_BODY).rating(UPDATED_RATING);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedReview.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedReview))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReview.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testReview.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    void putNonExistingReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().collectList().block().size();
        review.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, review.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(review))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().collectList().block().size();
        review.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(review))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().collectList().block().size();
        review.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(review))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        reviewRepository.save(review).block();

        int databaseSizeBeforeUpdate = reviewRepository.findAll().collectList().block().size();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        partialUpdatedReview.rating(UPDATED_RATING);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReview.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReview))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testReview.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testReview.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    void fullUpdateReviewWithPatch() throws Exception {
        // Initialize the database
        reviewRepository.save(review).block();

        int databaseSizeBeforeUpdate = reviewRepository.findAll().collectList().block().size();

        // Update the review using partial update
        Review partialUpdatedReview = new Review();
        partialUpdatedReview.setId(review.getId());

        partialUpdatedReview.title(UPDATED_TITLE).body(UPDATED_BODY).rating(UPDATED_RATING);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedReview.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedReview))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
        Review testReview = reviewList.get(reviewList.size() - 1);
        assertThat(testReview.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testReview.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testReview.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    void patchNonExistingReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().collectList().block().size();
        review.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, review.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(review))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().collectList().block().size();
        review.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(review))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamReview() throws Exception {
        int databaseSizeBeforeUpdate = reviewRepository.findAll().collectList().block().size();
        review.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(review))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Review in the database
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteReview() {
        // Initialize the database
        reviewRepository.save(review).block();

        int databaseSizeBeforeDelete = reviewRepository.findAll().collectList().block().size();

        // Delete the review
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, review.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Review> reviewList = reviewRepository.findAll().collectList().block();
        assertThat(reviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
