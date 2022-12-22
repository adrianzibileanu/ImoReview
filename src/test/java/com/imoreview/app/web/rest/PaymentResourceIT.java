package com.imoreview.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.imoreview.app.IntegrationTest;
import com.imoreview.app.domain.Payment;
import com.imoreview.app.domain.enumeration.Currency;
import com.imoreview.app.repository.PaymentRepository;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class PaymentResourceIT {

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    private static final Currency DEFAULT_CURRENCY = Currency.RON;
    private static final Currency UPDATED_CURRENCY = Currency.EUR;

    private static final Boolean DEFAULT_TERMS = false;
    private static final Boolean UPDATED_TERMS = true;

    private static final Boolean DEFAULT_SUCCESS = false;
    private static final Boolean UPDATED_SUCCESS = true;

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private WebTestClient webTestClient;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity() {
        Payment payment = new Payment()
            .price(DEFAULT_PRICE)
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .terms(DEFAULT_TERMS)
            .success(DEFAULT_SUCCESS);
        return payment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity() {
        Payment payment = new Payment()
            .price(UPDATED_PRICE)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .terms(UPDATED_TERMS)
            .success(UPDATED_SUCCESS);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        paymentRepository.deleteAll().block();
        payment = createEntity();
    }

    @Test
    void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().collectList().block().size();
        // Create the Payment
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payment))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPayment.getAmount()).isEqualTo(DEFAULT_AMOUNT);
        assertThat(testPayment.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testPayment.getTerms()).isEqualTo(DEFAULT_TERMS);
        assertThat(testPayment.getSuccess()).isEqualTo(DEFAULT_SUCCESS);
    }

    @Test
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId("existing_id");

        int databaseSizeBeforeCreate = paymentRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().collectList().block().size();
        // set the field null
        payment.setAmount(null);

        // Create the Payment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTermsIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().collectList().block().size();
        // set the field null
        payment.setTerms(null);

        // Create the Payment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkSuccessIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().collectList().block().size();
        // set the field null
        payment.setSuccess(null);

        // Create the Payment, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllPayments() {
        // Initialize the database
        paymentRepository.save(payment).block();

        // Get all the paymentList
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
            .value(hasItem(payment.getId()))
            .jsonPath("$.[*].price")
            .value(hasItem(DEFAULT_PRICE.doubleValue()))
            .jsonPath("$.[*].amount")
            .value(hasItem(DEFAULT_AMOUNT.doubleValue()))
            .jsonPath("$.[*].currency")
            .value(hasItem(DEFAULT_CURRENCY.toString()))
            .jsonPath("$.[*].terms")
            .value(hasItem(DEFAULT_TERMS.booleanValue()))
            .jsonPath("$.[*].success")
            .value(hasItem(DEFAULT_SUCCESS.booleanValue()));
    }

    @Test
    void getPayment() {
        // Initialize the database
        paymentRepository.save(payment).block();

        // Get the payment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, payment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(payment.getId()))
            .jsonPath("$.price")
            .value(is(DEFAULT_PRICE.doubleValue()))
            .jsonPath("$.amount")
            .value(is(DEFAULT_AMOUNT.doubleValue()))
            .jsonPath("$.currency")
            .value(is(DEFAULT_CURRENCY.toString()))
            .jsonPath("$.terms")
            .value(is(DEFAULT_TERMS.booleanValue()))
            .jsonPath("$.success")
            .value(is(DEFAULT_SUCCESS.booleanValue()));
    }

    @Test
    void getNonExistingPayment() {
        // Get the payment
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingPayment() throws Exception {
        // Initialize the database
        paymentRepository.save(payment).block();

        int databaseSizeBeforeUpdate = paymentRepository.findAll().collectList().block().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).block();
        updatedPayment.price(UPDATED_PRICE).amount(UPDATED_AMOUNT).currency(UPDATED_CURRENCY).terms(UPDATED_TERMS).success(UPDATED_SUCCESS);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedPayment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedPayment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testPayment.getTerms()).isEqualTo(UPDATED_TERMS);
        assertThat(testPayment.getSuccess()).isEqualTo(UPDATED_SUCCESS);
    }

    @Test
    void putNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().collectList().block().size();
        payment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, payment.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().collectList().block().size();
        payment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().collectList().block().size();
        payment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(payment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.save(payment).block();

        int databaseSizeBeforeUpdate = paymentRepository.findAll().collectList().block().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment.amount(UPDATED_AMOUNT).success(UPDATED_SUCCESS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testPayment.getTerms()).isEqualTo(DEFAULT_TERMS);
        assertThat(testPayment.getSuccess()).isEqualTo(UPDATED_SUCCESS);
    }

    @Test
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.save(payment).block();

        int databaseSizeBeforeUpdate = paymentRepository.findAll().collectList().block().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .price(UPDATED_PRICE)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .terms(UPDATED_TERMS)
            .success(UPDATED_SUCCESS);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPayment.getAmount()).isEqualTo(UPDATED_AMOUNT);
        assertThat(testPayment.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testPayment.getTerms()).isEqualTo(UPDATED_TERMS);
        assertThat(testPayment.getSuccess()).isEqualTo(UPDATED_SUCCESS);
    }

    @Test
    void patchNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().collectList().block().size();
        payment.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, payment.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().collectList().block().size();
        payment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payment))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().collectList().block().size();
        payment.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(payment))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePayment() {
        // Initialize the database
        paymentRepository.save(payment).block();

        int databaseSizeBeforeDelete = paymentRepository.findAll().collectList().block().size();

        // Delete the payment
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, payment.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll().collectList().block();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
