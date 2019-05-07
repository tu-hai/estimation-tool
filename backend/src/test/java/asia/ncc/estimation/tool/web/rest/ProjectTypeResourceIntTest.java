package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.EstimationToolApp;

import asia.ncc.estimation.tool.domain.ProjectType;
import asia.ncc.estimation.tool.repository.ProjectTypeRepository;
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
 * Test class for the ProjectTypeResource REST controller.
 *
 * @see ProjectTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstimationToolApp.class)
public class ProjectTypeResourceIntTest {

    private static final String DEFAULT_DECRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DECRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TECHNOGORY = "AAAAAAAAAA";
    private static final String UPDATED_TECHNOGORY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private ProjectTypeRepository projectTypeRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProjectTypeMockMvc;

    private ProjectType projectType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectTypeResource projectTypeResource = new ProjectTypeResource(projectTypeRepository);
        this.restProjectTypeMockMvc = MockMvcBuilders.standaloneSetup(projectTypeResource)
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
    public static ProjectType createEntity(EntityManager em) {
        ProjectType projectType = new ProjectType()
            .decription(DEFAULT_DECRIPTION)
            .technogory(DEFAULT_TECHNOGORY)
            .name(DEFAULT_NAME);
        return projectType;
    }

    @Before
    public void initTest() {
        projectType = createEntity(em);
    }

    @Test
    @Transactional
    public void createProjectType() throws Exception {
        int databaseSizeBeforeCreate = projectTypeRepository.findAll().size();

        // Create the ProjectType
        restProjectTypeMockMvc.perform(post("/api/project-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectType)))
            .andExpect(status().isCreated());

        // Validate the ProjectType in the database
        List<ProjectType> projectTypeList = projectTypeRepository.findAll();
        assertThat(projectTypeList).hasSize(databaseSizeBeforeCreate + 1);
        ProjectType testProjectType = projectTypeList.get(projectTypeList.size() - 1);
        assertThat(testProjectType.getDecription()).isEqualTo(DEFAULT_DECRIPTION);
        assertThat(testProjectType.getTechnogory()).isEqualTo(DEFAULT_TECHNOGORY);
        assertThat(testProjectType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createProjectTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectTypeRepository.findAll().size();

        // Create the ProjectType with an existing ID
        projectType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectTypeMockMvc.perform(post("/api/project-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectType)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectType in the database
        List<ProjectType> projectTypeList = projectTypeRepository.findAll();
        assertThat(projectTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProjectTypes() throws Exception {
        // Initialize the database
        projectTypeRepository.saveAndFlush(projectType);

        // Get all the projectTypeList
        restProjectTypeMockMvc.perform(get("/api/project-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(projectType.getId().intValue())))
            .andExpect(jsonPath("$.[*].decription").value(hasItem(DEFAULT_DECRIPTION.toString())))
            .andExpect(jsonPath("$.[*].technogory").value(hasItem(DEFAULT_TECHNOGORY.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getProjectType() throws Exception {
        // Initialize the database
        projectTypeRepository.saveAndFlush(projectType);

        // Get the projectType
        restProjectTypeMockMvc.perform(get("/api/project-types/{id}", projectType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(projectType.getId().intValue()))
            .andExpect(jsonPath("$.decription").value(DEFAULT_DECRIPTION.toString()))
            .andExpect(jsonPath("$.technogory").value(DEFAULT_TECHNOGORY.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProjectType() throws Exception {
        // Get the projectType
        restProjectTypeMockMvc.perform(get("/api/project-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProjectType() throws Exception {
        // Initialize the database
        projectTypeRepository.saveAndFlush(projectType);

        int databaseSizeBeforeUpdate = projectTypeRepository.findAll().size();

        // Update the projectType
        ProjectType updatedProjectType = projectTypeRepository.findById(projectType.getId()).get();
        // Disconnect from session so that the updates on updatedProjectType are not directly saved in db
        em.detach(updatedProjectType);
        updatedProjectType
            .decription(UPDATED_DECRIPTION)
            .technogory(UPDATED_TECHNOGORY)
            .name(UPDATED_NAME);

        restProjectTypeMockMvc.perform(put("/api/project-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProjectType)))
            .andExpect(status().isOk());

        // Validate the ProjectType in the database
        List<ProjectType> projectTypeList = projectTypeRepository.findAll();
        assertThat(projectTypeList).hasSize(databaseSizeBeforeUpdate);
        ProjectType testProjectType = projectTypeList.get(projectTypeList.size() - 1);
        assertThat(testProjectType.getDecription()).isEqualTo(UPDATED_DECRIPTION);
        assertThat(testProjectType.getTechnogory()).isEqualTo(UPDATED_TECHNOGORY);
        assertThat(testProjectType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingProjectType() throws Exception {
        int databaseSizeBeforeUpdate = projectTypeRepository.findAll().size();

        // Create the ProjectType

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectTypeMockMvc.perform(put("/api/project-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(projectType)))
            .andExpect(status().isBadRequest());

        // Validate the ProjectType in the database
        List<ProjectType> projectTypeList = projectTypeRepository.findAll();
        assertThat(projectTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProjectType() throws Exception {
        // Initialize the database
        projectTypeRepository.saveAndFlush(projectType);

        int databaseSizeBeforeDelete = projectTypeRepository.findAll().size();

        // Get the projectType
        restProjectTypeMockMvc.perform(delete("/api/project-types/{id}", projectType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ProjectType> projectTypeList = projectTypeRepository.findAll();
        assertThat(projectTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProjectType.class);
        ProjectType projectType1 = new ProjectType();
        projectType1.setId(1L);
        ProjectType projectType2 = new ProjectType();
        projectType2.setId(projectType1.getId());
        assertThat(projectType1).isEqualTo(projectType2);
        projectType2.setId(2L);
        assertThat(projectType1).isNotEqualTo(projectType2);
        projectType1.setId(null);
        assertThat(projectType1).isNotEqualTo(projectType2);
    }
}
