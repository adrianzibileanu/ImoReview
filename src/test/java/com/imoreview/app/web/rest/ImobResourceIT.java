package com.imoreview.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.imoreview.app.IntegrationTest;
import com.imoreview.app.domain.Imob;
import com.imoreview.app.domain.enumeration.Currency;
import com.imoreview.app.domain.enumeration.ImobCateg;
import com.imoreview.app.domain.enumeration.ImobServ;
import com.imoreview.app.domain.enumeration.ImobType;
import com.imoreview.app.repository.ImobRepository;
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
 * Integration tests for the {@link ImobResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ImobResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ImobType DEFAULT_TYPE = ImobType.APT;
    private static final ImobType UPDATED_TYPE = ImobType.STD;

    private static final ImobCateg DEFAULT_CATEG = ImobCateg.RZD;
    private static final ImobCateg UPDATED_CATEG = ImobCateg.COMM;

    private static final ImobServ DEFAULT_SERVICE = ImobServ.RENT;
    private static final ImobServ UPDATED_SERVICE = ImobServ.BUY;

    private static final Double DEFAULT_PRICE = 1000.00D;
    private static final Double UPDATED_PRICE = 1001D;

    private static final Currency DEFAULT_PRICE_CURRENCY = Currency.RON;
    private static final Currency UPDATED_PRICE_CURRENCY = Currency.EUR;

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "0-9";
    private static final String UPDATED_CONTACT = "0-9B";

    private static final Integer DEFAULT_NBOF_ROOMS = 1;
    private static final Integer UPDATED_NBOF_ROOMS = 2;

    private static final LocalDate DEFAULT_CONSTR_YEAR = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CONSTR_YEAR = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_USE_SURFACE = 1D;
    private static final Double UPDATED_USE_SURFACE = 2D;

    private static final String DEFAULT_BUILT_SURFACE = "AAAAAAAAAA";
    private static final String UPDATED_BUILT_SURFACE = "BBBBBBBBBB";

    private static final String DEFAULT_COMPART = "AAAAAAAAAA";
    private static final String UPDATED_COMPART = "BBBBBBBBBB";

    private static final String DEFAULT_CONFORT = "AAAAAAAAAA";
    private static final String UPDATED_CONFORT = "BBBBBBBBBB";

    private static final Integer DEFAULT_FLOOR = 1;
    private static final Integer UPDATED_FLOOR = 2;

    private static final Integer DEFAULT_NBOF_KITCHENS = 1;
    private static final Integer UPDATED_NBOF_KITCHENS = 2;

    private static final String DEFAULT_NBOF_BTHROOMS = "AAAAAAAAAA";
    private static final String UPDATED_NBOF_BTHROOMS = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT_HEIGHT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT_HEIGHT = "BBBBBBBBBB";

    private static final String DEFAULT_NBOF_BALCONIES = "AAAAAAAAAA";
    private static final String UPDATED_NBOF_BALCONIES = "BBBBBBBBBB";

    private static final String DEFAULT_UTILITIES = "AAAAAAAAAA";
    private static final String UPDATED_UTILITIES = "BBBBBBBBBB";

    private static final String DEFAULT_FEATURES = "AAAAAAAAAA";
    private static final String UPDATED_FEATURES = "BBBBBBBBBB";

    private static final String DEFAULT_OTHERDETAILS = "AAAAAAAAAA";
    private static final String UPDATED_OTHERDETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_ZONE_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ZONE_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_AVAILABILITY = "AAAAAAAAAA";
    private static final String UPDATED_AVAILABILITY = "BBBBBBBBBB";

    private static final String DEFAULT_OWNERID = "AAAAAAAAAA";
    private static final String UPDATED_OWNERID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/imobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ImobRepository imobRepository;

    @Mock
    private ImobRepository imobRepositoryMock;

    @Autowired
    private WebTestClient webTestClient;

    private Imob imob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Imob createEntity() {
        Imob imob = new Imob()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .type(DEFAULT_TYPE)
            .categ(DEFAULT_CATEG)
            .service(DEFAULT_SERVICE)
            .price(DEFAULT_PRICE)
            .priceCurrency(DEFAULT_PRICE_CURRENCY)
            .tags(DEFAULT_TAGS)
            .address(DEFAULT_ADDRESS)
            .contact(DEFAULT_CONTACT)
            .nbofRooms(DEFAULT_NBOF_ROOMS)
            .constrYear(DEFAULT_CONSTR_YEAR)
            .useSurface(DEFAULT_USE_SURFACE)
            .builtSurface(DEFAULT_BUILT_SURFACE)
            .compart(DEFAULT_COMPART)
            .confort(DEFAULT_CONFORT)
            .floor(DEFAULT_FLOOR)
            .nbofKitchens(DEFAULT_NBOF_KITCHENS)
            .nbofBthrooms(DEFAULT_NBOF_BTHROOMS)
            .unitType(DEFAULT_UNIT_TYPE)
            .unitHeight(DEFAULT_UNIT_HEIGHT)
            .nbofBalconies(DEFAULT_NBOF_BALCONIES)
            .utilities(DEFAULT_UTILITIES)
            .features(DEFAULT_FEATURES)
            .otherdetails(DEFAULT_OTHERDETAILS)
            .zoneDetails(DEFAULT_ZONE_DETAILS)
            .availability(DEFAULT_AVAILABILITY);
        // .ownerid(DEFAULT_OWNERID);
        return imob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Imob createUpdatedEntity() {
        Imob imob = new Imob()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .categ(UPDATED_CATEG)
            .service(UPDATED_SERVICE)
            .price(UPDATED_PRICE)
            .priceCurrency(UPDATED_PRICE_CURRENCY)
            .tags(UPDATED_TAGS)
            .address(UPDATED_ADDRESS)
            .contact(UPDATED_CONTACT)
            .nbofRooms(UPDATED_NBOF_ROOMS)
            .constrYear(UPDATED_CONSTR_YEAR)
            .useSurface(UPDATED_USE_SURFACE)
            .builtSurface(UPDATED_BUILT_SURFACE)
            .compart(UPDATED_COMPART)
            .confort(UPDATED_CONFORT)
            .floor(UPDATED_FLOOR)
            .nbofKitchens(UPDATED_NBOF_KITCHENS)
            .nbofBthrooms(UPDATED_NBOF_BTHROOMS)
            .unitType(UPDATED_UNIT_TYPE)
            .unitHeight(UPDATED_UNIT_HEIGHT)
            .nbofBalconies(UPDATED_NBOF_BALCONIES)
            .utilities(UPDATED_UTILITIES)
            .features(UPDATED_FEATURES)
            .otherdetails(UPDATED_OTHERDETAILS)
            .zoneDetails(UPDATED_ZONE_DETAILS)
            .availability(UPDATED_AVAILABILITY);
        //  .ownerid(UPDATED_OWNERID);
        return imob;
    }

    @BeforeEach
    public void initTest() {
        imobRepository.deleteAll().block();
        imob = createEntity();
    }

    @Test
    void createImob() throws Exception {
        int databaseSizeBeforeCreate = imobRepository.findAll().collectList().block().size();
        // Create the Imob
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the Imob in the database
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeCreate + 1);
        Imob testImob = imobList.get(imobList.size() - 1);
        assertThat(testImob.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testImob.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testImob.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testImob.getCateg()).isEqualTo(DEFAULT_CATEG);
        assertThat(testImob.getService()).isEqualTo(DEFAULT_SERVICE);
        assertThat(testImob.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testImob.getPriceCurrency()).isEqualTo(DEFAULT_PRICE_CURRENCY);
        assertThat(testImob.getTags()).isEqualTo(DEFAULT_TAGS);
        assertThat(testImob.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testImob.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testImob.getNbofRooms()).isEqualTo(DEFAULT_NBOF_ROOMS);
        assertThat(testImob.getConstrYear()).isEqualTo(DEFAULT_CONSTR_YEAR);
        assertThat(testImob.getUseSurface()).isEqualTo(DEFAULT_USE_SURFACE);
        assertThat(testImob.getBuiltSurface()).isEqualTo(DEFAULT_BUILT_SURFACE);
        assertThat(testImob.getCompart()).isEqualTo(DEFAULT_COMPART);
        assertThat(testImob.getConfort()).isEqualTo(DEFAULT_CONFORT);
        assertThat(testImob.getFloor()).isEqualTo(DEFAULT_FLOOR);
        assertThat(testImob.getNbofKitchens()).isEqualTo(DEFAULT_NBOF_KITCHENS);
        assertThat(testImob.getNbofBthrooms()).isEqualTo(DEFAULT_NBOF_BTHROOMS);
        assertThat(testImob.getUnitType()).isEqualTo(DEFAULT_UNIT_TYPE);
        assertThat(testImob.getUnitHeight()).isEqualTo(DEFAULT_UNIT_HEIGHT);
        assertThat(testImob.getNbofBalconies()).isEqualTo(DEFAULT_NBOF_BALCONIES);
        assertThat(testImob.getUtilities()).isEqualTo(DEFAULT_UTILITIES);
        assertThat(testImob.getFeatures()).isEqualTo(DEFAULT_FEATURES);
        assertThat(testImob.getOtherdetails()).isEqualTo(DEFAULT_OTHERDETAILS);
        assertThat(testImob.getZoneDetails()).isEqualTo(DEFAULT_ZONE_DETAILS);
        assertThat(testImob.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
        // assertThat(testImob.getOwnerid()).isEqualTo(DEFAULT_OWNERID);
    }

    @Test
    void createImobWithExistingId() throws Exception {
        // Create the Imob with an existing ID
        imob.setId("existing_id");

        int databaseSizeBeforeCreate = imobRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Imob in the database
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setTitle(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setDescription(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setType(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkCategIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setCateg(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkServiceIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setService(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setPrice(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkPriceCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setPriceCurrency(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setAddress(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setContact(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNbofRoomsIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setNbofRooms(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkConstrYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setConstrYear(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkUseSurfaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setUseSurface(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkBuiltSurfaceIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setBuiltSurface(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkConfortIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setConfort(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNbofKitchensIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setNbofKitchens(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkNbofBthroomsIsRequired() throws Exception {
        int databaseSizeBeforeTest = imobRepository.findAll().collectList().block().size();
        // set the field null
        imob.setNbofBthrooms(null);

        // Create the Imob, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllImobs() {
        // Initialize the database
        imobRepository.save(imob).block();

        // Get all the imobList
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
            .value(hasItem(imob.getId()))
            .jsonPath("$.[*].title")
            .value(hasItem(DEFAULT_TITLE))
            .jsonPath("$.[*].description")
            .value(hasItem(DEFAULT_DESCRIPTION))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE.toString()))
            .jsonPath("$.[*].categ")
            .value(hasItem(DEFAULT_CATEG.toString()))
            .jsonPath("$.[*].service")
            .value(hasItem(DEFAULT_SERVICE.toString()))
            .jsonPath("$.[*].price")
            .value(hasItem(DEFAULT_PRICE.doubleValue()))
            .jsonPath("$.[*].priceCurrency")
            .value(hasItem(DEFAULT_PRICE_CURRENCY.toString()))
            .jsonPath("$.[*].tags")
            .value(hasItem(DEFAULT_TAGS))
            .jsonPath("$.[*].address")
            .value(hasItem(DEFAULT_ADDRESS))
            .jsonPath("$.[*].contact")
            .value(hasItem(DEFAULT_CONTACT))
            .jsonPath("$.[*].nbofRooms")
            .value(hasItem(DEFAULT_NBOF_ROOMS))
            .jsonPath("$.[*].constrYear")
            .value(hasItem(DEFAULT_CONSTR_YEAR.toString()))
            .jsonPath("$.[*].useSurface")
            .value(hasItem(DEFAULT_USE_SURFACE.doubleValue()))
            .jsonPath("$.[*].builtSurface")
            .value(hasItem(DEFAULT_BUILT_SURFACE))
            .jsonPath("$.[*].compart")
            .value(hasItem(DEFAULT_COMPART))
            .jsonPath("$.[*].confort")
            .value(hasItem(DEFAULT_CONFORT))
            .jsonPath("$.[*].floor")
            .value(hasItem(DEFAULT_FLOOR))
            .jsonPath("$.[*].nbofKitchens")
            .value(hasItem(DEFAULT_NBOF_KITCHENS))
            .jsonPath("$.[*].nbofBthrooms")
            .value(hasItem(DEFAULT_NBOF_BTHROOMS))
            .jsonPath("$.[*].unitType")
            .value(hasItem(DEFAULT_UNIT_TYPE))
            .jsonPath("$.[*].unitHeight")
            .value(hasItem(DEFAULT_UNIT_HEIGHT))
            .jsonPath("$.[*].nbofBalconies")
            .value(hasItem(DEFAULT_NBOF_BALCONIES))
            .jsonPath("$.[*].utilities")
            .value(hasItem(DEFAULT_UTILITIES))
            .jsonPath("$.[*].features")
            .value(hasItem(DEFAULT_FEATURES))
            .jsonPath("$.[*].otherdetails")
            .value(hasItem(DEFAULT_OTHERDETAILS))
            .jsonPath("$.[*].zoneDetails")
            .value(hasItem(DEFAULT_ZONE_DETAILS))
            .jsonPath("$.[*].availability")
            .value(hasItem(DEFAULT_AVAILABILITY))
            .jsonPath("$.[*].ownerid")
            .value(hasItem(DEFAULT_OWNERID));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImobsWithEagerRelationshipsIsEnabled() {
        when(imobRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=true").exchange().expectStatus().isOk();

        verify(imobRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllImobsWithEagerRelationshipsIsNotEnabled() {
        when(imobRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(Flux.empty());

        webTestClient.get().uri(ENTITY_API_URL + "?eagerload=false").exchange().expectStatus().isOk();
        verify(imobRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    void getImob() {
        // Initialize the database
        imobRepository.save(imob).block();

        // Get the imob
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, imob.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(imob.getId()))
            .jsonPath("$.title")
            .value(is(DEFAULT_TITLE))
            .jsonPath("$.description")
            .value(is(DEFAULT_DESCRIPTION))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE.toString()))
            .jsonPath("$.categ")
            .value(is(DEFAULT_CATEG.toString()))
            .jsonPath("$.service")
            .value(is(DEFAULT_SERVICE.toString()))
            .jsonPath("$.price")
            .value(is(DEFAULT_PRICE.doubleValue()))
            .jsonPath("$.priceCurrency")
            .value(is(DEFAULT_PRICE_CURRENCY.toString()))
            .jsonPath("$.tags")
            .value(is(DEFAULT_TAGS))
            .jsonPath("$.address")
            .value(is(DEFAULT_ADDRESS))
            .jsonPath("$.contact")
            .value(is(DEFAULT_CONTACT))
            .jsonPath("$.nbofRooms")
            .value(is(DEFAULT_NBOF_ROOMS))
            .jsonPath("$.constrYear")
            .value(is(DEFAULT_CONSTR_YEAR.toString()))
            .jsonPath("$.useSurface")
            .value(is(DEFAULT_USE_SURFACE.doubleValue()))
            .jsonPath("$.builtSurface")
            .value(is(DEFAULT_BUILT_SURFACE))
            .jsonPath("$.compart")
            .value(is(DEFAULT_COMPART))
            .jsonPath("$.confort")
            .value(is(DEFAULT_CONFORT))
            .jsonPath("$.floor")
            .value(is(DEFAULT_FLOOR))
            .jsonPath("$.nbofKitchens")
            .value(is(DEFAULT_NBOF_KITCHENS))
            .jsonPath("$.nbofBthrooms")
            .value(is(DEFAULT_NBOF_BTHROOMS))
            .jsonPath("$.unitType")
            .value(is(DEFAULT_UNIT_TYPE))
            .jsonPath("$.unitHeight")
            .value(is(DEFAULT_UNIT_HEIGHT))
            .jsonPath("$.nbofBalconies")
            .value(is(DEFAULT_NBOF_BALCONIES))
            .jsonPath("$.utilities")
            .value(is(DEFAULT_UTILITIES))
            .jsonPath("$.features")
            .value(is(DEFAULT_FEATURES))
            .jsonPath("$.otherdetails")
            .value(is(DEFAULT_OTHERDETAILS))
            .jsonPath("$.zoneDetails")
            .value(is(DEFAULT_ZONE_DETAILS))
            .jsonPath("$.availability")
            .value(is(DEFAULT_AVAILABILITY))
            .jsonPath("$.ownerid")
            .value(is(DEFAULT_OWNERID));
    }

    @Test
    void getNonExistingImob() {
        // Get the imob
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingImob() throws Exception {
        // Initialize the database
        imobRepository.save(imob).block();

        int databaseSizeBeforeUpdate = imobRepository.findAll().collectList().block().size();

        // Update the imob
        Imob updatedImob = imobRepository.findById(imob.getId()).block();
        updatedImob
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .categ(UPDATED_CATEG)
            .service(UPDATED_SERVICE)
            .price(UPDATED_PRICE)
            .priceCurrency(UPDATED_PRICE_CURRENCY)
            .tags(UPDATED_TAGS)
            .address(UPDATED_ADDRESS)
            .contact(UPDATED_CONTACT)
            .nbofRooms(UPDATED_NBOF_ROOMS)
            .constrYear(UPDATED_CONSTR_YEAR)
            .useSurface(UPDATED_USE_SURFACE)
            .builtSurface(UPDATED_BUILT_SURFACE)
            .compart(UPDATED_COMPART)
            .confort(UPDATED_CONFORT)
            .floor(UPDATED_FLOOR)
            .nbofKitchens(UPDATED_NBOF_KITCHENS)
            .nbofBthrooms(UPDATED_NBOF_BTHROOMS)
            .unitType(UPDATED_UNIT_TYPE)
            .unitHeight(UPDATED_UNIT_HEIGHT)
            .nbofBalconies(UPDATED_NBOF_BALCONIES)
            .utilities(UPDATED_UTILITIES)
            .features(UPDATED_FEATURES)
            .otherdetails(UPDATED_OTHERDETAILS)
            .zoneDetails(UPDATED_ZONE_DETAILS)
            .availability(UPDATED_AVAILABILITY);
        //  .ownerid(UPDATED_OWNERID);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedImob.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedImob))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Imob in the database
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeUpdate);
        Imob testImob = imobList.get(imobList.size() - 1);
        assertThat(testImob.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImob.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testImob.getCateg()).isEqualTo(UPDATED_CATEG);
        assertThat(testImob.getService()).isEqualTo(UPDATED_SERVICE);
        assertThat(testImob.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testImob.getPriceCurrency()).isEqualTo(UPDATED_PRICE_CURRENCY);
        assertThat(testImob.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testImob.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testImob.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testImob.getNbofRooms()).isEqualTo(UPDATED_NBOF_ROOMS);
        assertThat(testImob.getConstrYear()).isEqualTo(UPDATED_CONSTR_YEAR);
        assertThat(testImob.getUseSurface()).isEqualTo(UPDATED_USE_SURFACE);
        assertThat(testImob.getBuiltSurface()).isEqualTo(UPDATED_BUILT_SURFACE);
        assertThat(testImob.getCompart()).isEqualTo(UPDATED_COMPART);
        assertThat(testImob.getConfort()).isEqualTo(UPDATED_CONFORT);
        assertThat(testImob.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testImob.getNbofKitchens()).isEqualTo(UPDATED_NBOF_KITCHENS);
        assertThat(testImob.getNbofBthrooms()).isEqualTo(UPDATED_NBOF_BTHROOMS);
        assertThat(testImob.getUnitType()).isEqualTo(UPDATED_UNIT_TYPE);
        assertThat(testImob.getUnitHeight()).isEqualTo(UPDATED_UNIT_HEIGHT);
        assertThat(testImob.getNbofBalconies()).isEqualTo(UPDATED_NBOF_BALCONIES);
        assertThat(testImob.getUtilities()).isEqualTo(UPDATED_UTILITIES);
        assertThat(testImob.getFeatures()).isEqualTo(UPDATED_FEATURES);
        assertThat(testImob.getOtherdetails()).isEqualTo(UPDATED_OTHERDETAILS);
        assertThat(testImob.getZoneDetails()).isEqualTo(UPDATED_ZONE_DETAILS);
        assertThat(testImob.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        //assertThat(testImob.getOwnerid()).isEqualTo(UPDATED_OWNERID);
    }

    @Test
    void putNonExistingImob() throws Exception {
        int databaseSizeBeforeUpdate = imobRepository.findAll().collectList().block().size();
        imob.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, imob.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Imob in the database
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchImob() throws Exception {
        int databaseSizeBeforeUpdate = imobRepository.findAll().collectList().block().size();
        imob.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Imob in the database
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamImob() throws Exception {
        int databaseSizeBeforeUpdate = imobRepository.findAll().collectList().block().size();
        imob.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Imob in the database
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateImobWithPatch() throws Exception {
        // Initialize the database
        imobRepository.save(imob).block();

        int databaseSizeBeforeUpdate = imobRepository.findAll().collectList().block().size();

        // Update the imob using partial update
        Imob partialUpdatedImob = new Imob();
        partialUpdatedImob.setId(imob.getId());

        partialUpdatedImob
            .description(UPDATED_DESCRIPTION)
            .categ(UPDATED_CATEG)
            .service(UPDATED_SERVICE)
            .priceCurrency(UPDATED_PRICE_CURRENCY)
            .tags(UPDATED_TAGS)
            .contact(UPDATED_CONTACT)
            .constrYear(UPDATED_CONSTR_YEAR)
            .nbofKitchens(UPDATED_NBOF_KITCHENS)
            .utilities(UPDATED_UTILITIES)
            .features(UPDATED_FEATURES)
            .otherdetails(UPDATED_OTHERDETAILS);
        // .ownerid(UPDATED_OWNERID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedImob.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedImob))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Imob in the database
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeUpdate);
        Imob testImob = imobList.get(imobList.size() - 1);
        assertThat(testImob.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testImob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImob.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testImob.getCateg()).isEqualTo(UPDATED_CATEG);
        assertThat(testImob.getService()).isEqualTo(UPDATED_SERVICE);
        assertThat(testImob.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testImob.getPriceCurrency()).isEqualTo(UPDATED_PRICE_CURRENCY);
        assertThat(testImob.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testImob.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testImob.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testImob.getNbofRooms()).isEqualTo(DEFAULT_NBOF_ROOMS);
        assertThat(testImob.getConstrYear()).isEqualTo(UPDATED_CONSTR_YEAR);
        assertThat(testImob.getUseSurface()).isEqualTo(DEFAULT_USE_SURFACE);
        assertThat(testImob.getBuiltSurface()).isEqualTo(DEFAULT_BUILT_SURFACE);
        assertThat(testImob.getCompart()).isEqualTo(DEFAULT_COMPART);
        assertThat(testImob.getConfort()).isEqualTo(DEFAULT_CONFORT);
        assertThat(testImob.getFloor()).isEqualTo(DEFAULT_FLOOR);
        assertThat(testImob.getNbofKitchens()).isEqualTo(UPDATED_NBOF_KITCHENS);
        assertThat(testImob.getNbofBthrooms()).isEqualTo(DEFAULT_NBOF_BTHROOMS);
        assertThat(testImob.getUnitType()).isEqualTo(DEFAULT_UNIT_TYPE);
        assertThat(testImob.getUnitHeight()).isEqualTo(DEFAULT_UNIT_HEIGHT);
        assertThat(testImob.getNbofBalconies()).isEqualTo(DEFAULT_NBOF_BALCONIES);
        assertThat(testImob.getUtilities()).isEqualTo(UPDATED_UTILITIES);
        assertThat(testImob.getFeatures()).isEqualTo(UPDATED_FEATURES);
        assertThat(testImob.getOtherdetails()).isEqualTo(UPDATED_OTHERDETAILS);
        assertThat(testImob.getZoneDetails()).isEqualTo(DEFAULT_ZONE_DETAILS);
        assertThat(testImob.getAvailability()).isEqualTo(DEFAULT_AVAILABILITY);
        // assertThat(testImob.getOwnerid()).isEqualTo(UPDATED_OWNERID);
    }

    @Test
    void fullUpdateImobWithPatch() throws Exception {
        // Initialize the database
        imobRepository.save(imob).block();

        int databaseSizeBeforeUpdate = imobRepository.findAll().collectList().block().size();

        // Update the imob using partial update
        Imob partialUpdatedImob = new Imob();
        partialUpdatedImob.setId(imob.getId());

        partialUpdatedImob
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .type(UPDATED_TYPE)
            .categ(UPDATED_CATEG)
            .service(UPDATED_SERVICE)
            .price(UPDATED_PRICE)
            .priceCurrency(UPDATED_PRICE_CURRENCY)
            .tags(UPDATED_TAGS)
            .address(UPDATED_ADDRESS)
            .contact(UPDATED_CONTACT)
            .nbofRooms(UPDATED_NBOF_ROOMS)
            .constrYear(UPDATED_CONSTR_YEAR)
            .useSurface(UPDATED_USE_SURFACE)
            .builtSurface(UPDATED_BUILT_SURFACE)
            .compart(UPDATED_COMPART)
            .confort(UPDATED_CONFORT)
            .floor(UPDATED_FLOOR)
            .nbofKitchens(UPDATED_NBOF_KITCHENS)
            .nbofBthrooms(UPDATED_NBOF_BTHROOMS)
            .unitType(UPDATED_UNIT_TYPE)
            .unitHeight(UPDATED_UNIT_HEIGHT)
            .nbofBalconies(UPDATED_NBOF_BALCONIES)
            .utilities(UPDATED_UTILITIES)
            .features(UPDATED_FEATURES)
            .otherdetails(UPDATED_OTHERDETAILS)
            .zoneDetails(UPDATED_ZONE_DETAILS)
            .availability(UPDATED_AVAILABILITY);
        //  .ownerid(UPDATED_OWNERID);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedImob.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedImob))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the Imob in the database
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeUpdate);
        Imob testImob = imobList.get(imobList.size() - 1);
        assertThat(testImob.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testImob.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testImob.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testImob.getCateg()).isEqualTo(UPDATED_CATEG);
        assertThat(testImob.getService()).isEqualTo(UPDATED_SERVICE);
        assertThat(testImob.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testImob.getPriceCurrency()).isEqualTo(UPDATED_PRICE_CURRENCY);
        assertThat(testImob.getTags()).isEqualTo(UPDATED_TAGS);
        assertThat(testImob.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testImob.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testImob.getNbofRooms()).isEqualTo(UPDATED_NBOF_ROOMS);
        assertThat(testImob.getConstrYear()).isEqualTo(UPDATED_CONSTR_YEAR);
        assertThat(testImob.getUseSurface()).isEqualTo(UPDATED_USE_SURFACE);
        assertThat(testImob.getBuiltSurface()).isEqualTo(UPDATED_BUILT_SURFACE);
        assertThat(testImob.getCompart()).isEqualTo(UPDATED_COMPART);
        assertThat(testImob.getConfort()).isEqualTo(UPDATED_CONFORT);
        assertThat(testImob.getFloor()).isEqualTo(UPDATED_FLOOR);
        assertThat(testImob.getNbofKitchens()).isEqualTo(UPDATED_NBOF_KITCHENS);
        assertThat(testImob.getNbofBthrooms()).isEqualTo(UPDATED_NBOF_BTHROOMS);
        assertThat(testImob.getUnitType()).isEqualTo(UPDATED_UNIT_TYPE);
        assertThat(testImob.getUnitHeight()).isEqualTo(UPDATED_UNIT_HEIGHT);
        assertThat(testImob.getNbofBalconies()).isEqualTo(UPDATED_NBOF_BALCONIES);
        assertThat(testImob.getUtilities()).isEqualTo(UPDATED_UTILITIES);
        assertThat(testImob.getFeatures()).isEqualTo(UPDATED_FEATURES);
        assertThat(testImob.getOtherdetails()).isEqualTo(UPDATED_OTHERDETAILS);
        assertThat(testImob.getZoneDetails()).isEqualTo(UPDATED_ZONE_DETAILS);
        assertThat(testImob.getAvailability()).isEqualTo(UPDATED_AVAILABILITY);
        // assertThat(testImob.getOwnerid()).isEqualTo(UPDATED_OWNERID);
    }

    @Test
    void patchNonExistingImob() throws Exception {
        int databaseSizeBeforeUpdate = imobRepository.findAll().collectList().block().size();
        imob.setId(UUID.randomUUID().toString());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, imob.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Imob in the database
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchImob() throws Exception {
        int databaseSizeBeforeUpdate = imobRepository.findAll().collectList().block().size();
        imob.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, UUID.randomUUID().toString())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the Imob in the database
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamImob() throws Exception {
        int databaseSizeBeforeUpdate = imobRepository.findAll().collectList().block().size();
        imob.setId(UUID.randomUUID().toString());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(imob))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the Imob in the database
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteImob() {
        // Initialize the database
        imobRepository.save(imob).block();

        int databaseSizeBeforeDelete = imobRepository.findAll().collectList().block().size();

        // Delete the imob
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, imob.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<Imob> imobList = imobRepository.findAll().collectList().block();
        assertThat(imobList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
