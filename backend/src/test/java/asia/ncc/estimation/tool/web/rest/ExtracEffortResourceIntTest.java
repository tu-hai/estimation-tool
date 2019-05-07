package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.EstimationToolApp;

import asia.ncc.estimation.tool.domain.ExtracEffort;
import asia.ncc.estimation.tool.repository.ExtracEffortRepository;
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

import javax.persistence.EntityManager;
import java.util.List;


import static asia.ncc.estimation.tool.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExtracEffortResource REST controller.
 *
 * @see ExtracEffortResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstimationToolApp.class)
public class ExtracEffortResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DECRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DECRIPTION = "BBBBBBBBBB";

    @Autowired
    private ExtracEffortRepository extracEffortRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExtracEffortMockMvc;

    private ExtracEffort extracEffort;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExtracEffortResource extracEffortResource = new ExtracEffortResource(extracEffortRepository);
        this.restExtracEffortMockMvc = MockMvcBuilders.standaloneSetup(extracEffortResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtracEffort createEntity(EntityManager em) {
        ExtracEffort extracEffort = new ExtracEffort()
            .name(DEFAULT_NAME)
            .decription(DEFAULT_DECRIPTION);
        return extracEffort;
    }

    @Before
    public void initTest() {
        extracEffort = createEntity(em);
    }

    @Test
    @Transactional
    public void createExtracEffort() throws Exception {
        int databaseSizeBeforeCreate = extracEffortRepository.findAll().size();

        // Create the ExtracEffort
        restExtracEffortMockMvc.perform(post("/api/extrac-efforts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extracEffort)))
            .andExpect(status().isCreated());

        // Validate the ExtracEffort in the database
        List<ExtracEffort> extracEffortList = extracEffortRepository.findAll();
        assertThat(extracEffortList).hasSize(databaseSizeBeforeCreate + 1);
        ExtracEffort testExtracEffort = extracEffortList.get(extracEffortList.size() - 1);
        assertThat(testExtracEffort.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExtracEffort.getDecription()).isEqualTo(DEFAULT_DECRIPTION);
    }

    @Test
    @Transactional
    public void createExtracEffortWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = extracEffortRepository.findAll().size();

        // Create the ExtracEffort with an existing ID
        extracEffort.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtracEffortMockMvc.perform(post("/api/extrac-efforts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extracEffort)))
            .andExpect(status().isBadRequest());

        // Validate the ExtracEffort in the database
        List<ExtracEffort> extracEffortList = extracEffortRepository.findAll();
        assertThat(extracEffortList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExtracEfforts() throws Exception {
        // Initialize the database
        extracEffortRepository.saveAndFlush(extracEffort);

        // Get all the extracEffortList
        restExtracEffortMockMvc.perform(get("/api/extrac-efforts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extracEffort.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].decription").value(hasItem(DEFAULT_DECRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getExtracEffort() throws Exception {
        // Initialize the database
        extracEffortRepository.saveAndFlush(extracEffort);

        // Get the extracEffort
        restExtracEffortMockMvc.perform(get("/api/extrac-efforts/{id}", extracEffort.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(extracEffort.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.decription").value(DEFAULT_DECRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExtracEffort() throws Exception {
        // Get the extracEffort
        restExtracEffortMockMvc.perform(get("/api/extrac-efforts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExtracEffort() throws Exception {
        // Initialize the database
        extracEffortRepository.saveAndFlush(extracEffort);

        int databaseSizeBeforeUpdate = extracEffortRepository.findAll().size();

        // Update the extracEffort
        ExtracEffort updatedExtracEffort = extracEffortRepository.findById(extracEffort.getId()).get();
        // Disconnect from session so that the updates on updatedExtracEffort are not directly saved in db
        em.detach(updatedExtracEffort);
        updatedExtracEffort
            .name(UPDATED_NAME)
            .decription(UPDATED_DECRIPTION);

        restExtracEffortMockMvc.perform(put("/api/extrac-efforts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExtracEffort)))
            .andExpect(status().isOk());

        // Validate the ExtracEffort in the database
        List<ExtracEffort> extracEffortList = extracEffortRepository.findAll();
        assertThat(extracEffortList).hasSize(databaseSizeBeforeUpdate);
        ExtracEffort testExtracEffort = extracEffortList.get(extracEffortList.size() - 1);
        assertThat(testExtracEffort.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExtracEffort.getDecription()).isEqualTo(UPDATED_DECRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingExtracEffort() throws Exception {
        int databaseSizeBeforeUpdate = extracEffortRepository.findAll().size();

        // Create the ExtracEffort

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtracEffortMockMvc.perform(put("/api/extrac-efforts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(extracEffort)))
            .andExpect(status().isBadRequest());

        // Validate the ExtracEffort in the database
        List<ExtracEffort> extracEffortList = extracEffortRepository.findAll();
        assertThat(extracEffortList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExtracEffort() throws Exception {
        // Initialize the database
        extracEffortRepository.saveAndFlush(extracEffort);

        int databaseSizeBeforeDelete = extracEffortRepository.findAll().size();

        // Get the extracEffort
        restExtracEffortMockMvc.perform(delete("/api/extrac-efforts/{id}", extracEffort.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExtracEffort> extracEffortList = extracEffortRepository.findAll();
        assertThat(extracEffortList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExtracEffort.class);
        ExtracEffort extracEffort1 = new ExtracEffort();
        extracEffort1.setId(1L);
        ExtracEffort extracEffort2 = new ExtracEffort();
        extracEffort2.setId(extracEffort1.getId());
        assertThat(extracEffort1).isEqualTo(extracEffort2);
        extracEffort2.setId(2L);
        assertThat(extracEffort1).isNotEqualTo(extracEffort2);
        extracEffort1.setId(null);
        assertThat(extracEffort1).isNotEqualTo(extracEffort2);
    }
}
