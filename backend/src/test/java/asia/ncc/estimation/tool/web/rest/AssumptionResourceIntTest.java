package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.EstimationToolApp;

import asia.ncc.estimation.tool.domain.Assumption;
import asia.ncc.estimation.tool.repository.AssumptionRepository;
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
import java.util.List;


import static asia.ncc.estimation.tool.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AssumptionResource REST controller.
 *
 * @see AssumptionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstimationToolApp.class)
public class AssumptionResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    @Autowired
    private AssumptionRepository assumptionRepository;

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

    private MockMvc restAssumptionMockMvc;

    private Assumption assumption;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AssumptionResource assumptionResource = new AssumptionResource(assumptionRepository);
        this.restAssumptionMockMvc = MockMvcBuilders.standaloneSetup(assumptionResource)
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
    public static Assumption createEntity(EntityManager em) {
        Assumption assumption = new Assumption()
            .content(DEFAULT_CONTENT)
            .note(DEFAULT_NOTE);
        return assumption;
    }

    @Before
    public void initTest() {
        assumption = createEntity(em);
    }

    @Test
    @Transactional
    public void createAssumption() throws Exception {
        int databaseSizeBeforeCreate = assumptionRepository.findAll().size();

        // Create the Assumption
        restAssumptionMockMvc.perform(post("/api/assumptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assumption)))
            .andExpect(status().isCreated());

        // Validate the Assumption in the database
        List<Assumption> assumptionList = assumptionRepository.findAll();
        assertThat(assumptionList).hasSize(databaseSizeBeforeCreate + 1);
        Assumption testAssumption = assumptionList.get(assumptionList.size() - 1);
        assertThat(testAssumption.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testAssumption.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createAssumptionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = assumptionRepository.findAll().size();

        // Create the Assumption with an existing ID
        assumption.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssumptionMockMvc.perform(post("/api/assumptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assumption)))
            .andExpect(status().isBadRequest());

        // Validate the Assumption in the database
        List<Assumption> assumptionList = assumptionRepository.findAll();
        assertThat(assumptionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAssumptions() throws Exception {
        // Initialize the database
        assumptionRepository.saveAndFlush(assumption);

        // Get all the assumptionList
        restAssumptionMockMvc.perform(get("/api/assumptions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assumption.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }
    
    @Test
    @Transactional
    public void getAssumption() throws Exception {
        // Initialize the database
        assumptionRepository.saveAndFlush(assumption);

        // Get the assumption
        restAssumptionMockMvc.perform(get("/api/assumptions/{id}", assumption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(assumption.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAssumption() throws Exception {
        // Get the assumption
        restAssumptionMockMvc.perform(get("/api/assumptions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAssumption() throws Exception {
        // Initialize the database
        assumptionRepository.saveAndFlush(assumption);

        int databaseSizeBeforeUpdate = assumptionRepository.findAll().size();

        // Update the assumption
        Assumption updatedAssumption = assumptionRepository.findById(assumption.getId()).get();
        // Disconnect from session so that the updates on updatedAssumption are not directly saved in db
        em.detach(updatedAssumption);
        updatedAssumption
            .content(UPDATED_CONTENT)
            .note(UPDATED_NOTE);

        restAssumptionMockMvc.perform(put("/api/assumptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAssumption)))
            .andExpect(status().isOk());

        // Validate the Assumption in the database
        List<Assumption> assumptionList = assumptionRepository.findAll();
        assertThat(assumptionList).hasSize(databaseSizeBeforeUpdate);
        Assumption testAssumption = assumptionList.get(assumptionList.size() - 1);
        assertThat(testAssumption.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testAssumption.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingAssumption() throws Exception {
        int databaseSizeBeforeUpdate = assumptionRepository.findAll().size();

        // Create the Assumption

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssumptionMockMvc.perform(put("/api/assumptions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(assumption)))
            .andExpect(status().isBadRequest());

        // Validate the Assumption in the database
        List<Assumption> assumptionList = assumptionRepository.findAll();
        assertThat(assumptionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAssumption() throws Exception {
        // Initialize the database
        assumptionRepository.saveAndFlush(assumption);

        int databaseSizeBeforeDelete = assumptionRepository.findAll().size();

        // Get the assumption
        restAssumptionMockMvc.perform(delete("/api/assumptions/{id}", assumption.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Assumption> assumptionList = assumptionRepository.findAll();
        assertThat(assumptionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assumption.class);
        Assumption assumption1 = new Assumption();
        assumption1.setId(1L);
        Assumption assumption2 = new Assumption();
        assumption2.setId(assumption1.getId());
        assertThat(assumption1).isEqualTo(assumption2);
        assumption2.setId(2L);
        assertThat(assumption1).isNotEqualTo(assumption2);
        assumption1.setId(null);
        assertThat(assumption1).isNotEqualTo(assumption2);
    }
}
