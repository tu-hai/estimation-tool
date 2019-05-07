package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.EstimationToolApp;

import asia.ncc.estimation.tool.domain.FilePdf;
import asia.ncc.estimation.tool.repository.FilePdfRepository;
import asia.ncc.estimation.tool.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static asia.ncc.estimation.tool.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the FilePdfResource REST controller.
 *
 * @see FilePdfResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstimationToolApp.class)
public class FilePdfResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATE = LocalDate.now(ZoneId.systemDefault());

    private static final Long DEFAULT_ID_CUSTOMER = 1L;
    private static final Long UPDATED_ID_CUSTOMER = 2L;

    @Autowired
    private FilePdfRepository filePdfRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restFilePdfMockMvc;

    private FilePdf filePdf;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final FilePdfResource filePdfResource = new FilePdfResource(filePdfRepository);
        this.restFilePdfMockMvc = MockMvcBuilders.standaloneSetup(filePdfResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FilePdf createEntity(EntityManager em) {
        FilePdf filePdf = new FilePdf()
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .dateCreate(DEFAULT_DATE_CREATE)
            .idCustomer(DEFAULT_ID_CUSTOMER);
        return filePdf;
    }

    @Before
    public void initTest() {
        filePdf = createEntity(em);
    }

    @Test
    @Transactional
    public void createFilePdf() throws Exception {
        int databaseSizeBeforeCreate = filePdfRepository.findAll().size();

        // Create the FilePdf
        restFilePdfMockMvc.perform(post("/api/file-pdfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filePdf)))
            .andExpect(status().isCreated());

        // Validate the FilePdf in the database
        List<FilePdf> filePdfList = filePdfRepository.findAll();
        assertThat(filePdfList).hasSize(databaseSizeBeforeCreate + 1);
        FilePdf testFilePdf = filePdfList.get(filePdfList.size() - 1);
        assertThat(testFilePdf.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFilePdf.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testFilePdf.getDateCreate()).isEqualTo(DEFAULT_DATE_CREATE);
        assertThat(testFilePdf.getIdCustomer()).isEqualTo(DEFAULT_ID_CUSTOMER);
    }

    @Test
    @Transactional
    public void createFilePdfWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = filePdfRepository.findAll().size();

        // Create the FilePdf with an existing ID
        filePdf.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilePdfMockMvc.perform(post("/api/file-pdfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filePdf)))
            .andExpect(status().isBadRequest());

        // Validate the FilePdf in the database
        List<FilePdf> filePdfList = filePdfRepository.findAll();
        assertThat(filePdfList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllFilePdfs() throws Exception {
        // Initialize the database
        filePdfRepository.saveAndFlush(filePdf);

        // Get all the filePdfList
        restFilePdfMockMvc.perform(get("/api/file-pdfs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filePdf.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].dateCreate").value(hasItem(DEFAULT_DATE_CREATE.toString())))
            .andExpect(jsonPath("$.[*].idCustomer").value(hasItem(DEFAULT_ID_CUSTOMER.intValue())));
    }
    
    @Test
    @Transactional
    public void getFilePdf() throws Exception {
        // Initialize the database
        filePdfRepository.saveAndFlush(filePdf);

        // Get the filePdf
        restFilePdfMockMvc.perform(get("/api/file-pdfs/{id}", filePdf.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(filePdf.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.dateCreate").value(DEFAULT_DATE_CREATE.toString()))
            .andExpect(jsonPath("$.idCustomer").value(DEFAULT_ID_CUSTOMER.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFilePdf() throws Exception {
        // Get the filePdf
        restFilePdfMockMvc.perform(get("/api/file-pdfs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFilePdf() throws Exception {
        // Initialize the database
        filePdfRepository.saveAndFlush(filePdf);

        int databaseSizeBeforeUpdate = filePdfRepository.findAll().size();

        // Update the filePdf
        FilePdf updatedFilePdf = filePdfRepository.findById(filePdf.getId()).get();
        // Disconnect from session so that the updates on updatedFilePdf are not directly saved in db
        em.detach(updatedFilePdf);
        updatedFilePdf
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .dateCreate(UPDATED_DATE_CREATE)
            .idCustomer(UPDATED_ID_CUSTOMER);

        restFilePdfMockMvc.perform(put("/api/file-pdfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedFilePdf)))
            .andExpect(status().isOk());

        // Validate the FilePdf in the database
        List<FilePdf> filePdfList = filePdfRepository.findAll();
        assertThat(filePdfList).hasSize(databaseSizeBeforeUpdate);
        FilePdf testFilePdf = filePdfList.get(filePdfList.size() - 1);
        assertThat(testFilePdf.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFilePdf.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testFilePdf.getDateCreate()).isEqualTo(UPDATED_DATE_CREATE);
        assertThat(testFilePdf.getIdCustomer()).isEqualTo(UPDATED_ID_CUSTOMER);
    }

    @Test
    @Transactional
    public void updateNonExistingFilePdf() throws Exception {
        int databaseSizeBeforeUpdate = filePdfRepository.findAll().size();

        // Create the FilePdf

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilePdfMockMvc.perform(put("/api/file-pdfs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(filePdf)))
            .andExpect(status().isBadRequest());

        // Validate the FilePdf in the database
        List<FilePdf> filePdfList = filePdfRepository.findAll();
        assertThat(filePdfList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteFilePdf() throws Exception {
        // Initialize the database
        filePdfRepository.saveAndFlush(filePdf);

        int databaseSizeBeforeDelete = filePdfRepository.findAll().size();

        // Get the filePdf
        restFilePdfMockMvc.perform(delete("/api/file-pdfs/{id}", filePdf.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<FilePdf> filePdfList = filePdfRepository.findAll();
        assertThat(filePdfList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilePdf.class);
        FilePdf filePdf1 = new FilePdf();
        filePdf1.setId(1L);
        FilePdf filePdf2 = new FilePdf();
        filePdf2.setId(filePdf1.getId());
        assertThat(filePdf1).isEqualTo(filePdf2);
        filePdf2.setId(2L);
        assertThat(filePdf1).isNotEqualTo(filePdf2);
        filePdf1.setId(null);
        assertThat(filePdf1).isNotEqualTo(filePdf2);
    }
}
