package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.EstimationToolApp;

import asia.ncc.estimation.tool.domain.Tech;
import asia.ncc.estimation.tool.repository.TechRepository;
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
 * Test class for the TechResource REST controller.
 *
 * @see TechResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstimationToolApp.class)
public class TechResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TECH_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_TECH_CONTACT = "BBBBBBBBBB";

    @Autowired
    private TechRepository techRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTechMockMvc;

    private Tech tech;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TechResource techResource = new TechResource(techRepository);
        this.restTechMockMvc = MockMvcBuilders.standaloneSetup(techResource)
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
    public static Tech createEntity(EntityManager em) {
        Tech tech = new Tech()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .techContact(DEFAULT_TECH_CONTACT);
        return tech;
    }

    @Before
    public void initTest() {
        tech = createEntity(em);
    }

    @Test
    @Transactional
    public void createTech() throws Exception {
        int databaseSizeBeforeCreate = techRepository.findAll().size();

        // Create the Tech
        restTechMockMvc.perform(post("/api/teches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tech)))
            .andExpect(status().isCreated());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeCreate + 1);
        Tech testTech = techList.get(techList.size() - 1);
        assertThat(testTech.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTech.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTech.getTechContact()).isEqualTo(DEFAULT_TECH_CONTACT);
    }

    @Test
    @Transactional
    public void createTechWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = techRepository.findAll().size();

        // Create the Tech with an existing ID
        tech.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTechMockMvc.perform(post("/api/teches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tech)))
            .andExpect(status().isBadRequest());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTeches() throws Exception {
        // Initialize the database
        techRepository.saveAndFlush(tech);

        // Get all the techList
        restTechMockMvc.perform(get("/api/teches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tech.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].techContact").value(hasItem(DEFAULT_TECH_CONTACT.toString())));
    }
    
    @Test
    @Transactional
    public void getTech() throws Exception {
        // Initialize the database
        techRepository.saveAndFlush(tech);

        // Get the tech
        restTechMockMvc.perform(get("/api/teches/{id}", tech.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(tech.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.techContact").value(DEFAULT_TECH_CONTACT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTech() throws Exception {
        // Get the tech
        restTechMockMvc.perform(get("/api/teches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTech() throws Exception {
        // Initialize the database
        techRepository.saveAndFlush(tech);

        int databaseSizeBeforeUpdate = techRepository.findAll().size();

        // Update the tech
        Tech updatedTech = techRepository.findById(tech.getId()).get();
        // Disconnect from session so that the updates on updatedTech are not directly saved in db
        em.detach(updatedTech);
        updatedTech
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .techContact(UPDATED_TECH_CONTACT);

        restTechMockMvc.perform(put("/api/teches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTech)))
            .andExpect(status().isOk());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeUpdate);
        Tech testTech = techList.get(techList.size() - 1);
        assertThat(testTech.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTech.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTech.getTechContact()).isEqualTo(UPDATED_TECH_CONTACT);
    }

    @Test
    @Transactional
    public void updateNonExistingTech() throws Exception {
        int databaseSizeBeforeUpdate = techRepository.findAll().size();

        // Create the Tech

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTechMockMvc.perform(put("/api/teches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(tech)))
            .andExpect(status().isBadRequest());

        // Validate the Tech in the database
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTech() throws Exception {
        // Initialize the database
        techRepository.saveAndFlush(tech);

        int databaseSizeBeforeDelete = techRepository.findAll().size();

        // Get the tech
        restTechMockMvc.perform(delete("/api/teches/{id}", tech.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Tech> techList = techRepository.findAll();
        assertThat(techList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tech.class);
        Tech tech1 = new Tech();
        tech1.setId(1L);
        Tech tech2 = new Tech();
        tech2.setId(tech1.getId());
        assertThat(tech1).isEqualTo(tech2);
        tech2.setId(2L);
        assertThat(tech1).isNotEqualTo(tech2);
        tech1.setId(null);
        assertThat(tech1).isNotEqualTo(tech2);
    }
}
