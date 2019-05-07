package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.EstimationToolApp;

import asia.ncc.estimation.tool.domain.WorkItem;
import asia.ncc.estimation.tool.repository.WorkItemRepository;
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
 * Test class for the WorkItemResource REST controller.
 *
 * @see WorkItemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstimationToolApp.class)
public class WorkItemResourceIntTest {

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Long DEFAULT_ACTUALEFFORT = 1L;
    private static final Long UPDATED_ACTUALEFFORT = 2L;

    private static final String DEFAULT_TASKNAME = "AAAAAAAAAA";
    private static final String UPDATED_TASKNAME = "BBBBBBBBBB";

    private static final Long DEFAULT_CODING_EFFORT = 1L;
    private static final Long UPDATED_CODING_EFFORT = 2L;

    private static final Integer DEFAULT_INDATES = 1;
    private static final Integer UPDATED_INDATES = 2;

    @Autowired
    private WorkItemRepository workItemRepository;

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

    private MockMvc restWorkItemMockMvc;

    private WorkItem workItem;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final WorkItemResource workItemResource = new WorkItemResource(workItemRepository);
        this.restWorkItemMockMvc = MockMvcBuilders.standaloneSetup(workItemResource)
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
    public static WorkItem createEntity(EntityManager em) {
        WorkItem workItem = new WorkItem()
            .note(DEFAULT_NOTE)
            .actualeffort(DEFAULT_ACTUALEFFORT)
            .taskname(DEFAULT_TASKNAME)
            .codingEffort(DEFAULT_CODING_EFFORT)
            .indates(DEFAULT_INDATES);
        return workItem;
    }

    @Before
    public void initTest() {
        workItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createWorkItem() throws Exception {
        int databaseSizeBeforeCreate = workItemRepository.findAll().size();

        // Create the WorkItem
        restWorkItemMockMvc.perform(post("/api/work-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workItem)))
            .andExpect(status().isCreated());

        // Validate the WorkItem in the database
        List<WorkItem> workItemList = workItemRepository.findAll();
        assertThat(workItemList).hasSize(databaseSizeBeforeCreate + 1);
        WorkItem testWorkItem = workItemList.get(workItemList.size() - 1);
        assertThat(testWorkItem.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testWorkItem.getActualeffort()).isEqualTo(DEFAULT_ACTUALEFFORT);
        assertThat(testWorkItem.getTaskname()).isEqualTo(DEFAULT_TASKNAME);
        assertThat(testWorkItem.getCodingEffort()).isEqualTo(DEFAULT_CODING_EFFORT);
        assertThat(testWorkItem.getIndates()).isEqualTo(DEFAULT_INDATES);
    }

    @Test
    @Transactional
    public void createWorkItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = workItemRepository.findAll().size();

        // Create the WorkItem with an existing ID
        workItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkItemMockMvc.perform(post("/api/work-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workItem)))
            .andExpect(status().isBadRequest());

        // Validate the WorkItem in the database
        List<WorkItem> workItemList = workItemRepository.findAll();
        assertThat(workItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllWorkItems() throws Exception {
        // Initialize the database
        workItemRepository.saveAndFlush(workItem);

        // Get all the workItemList
        restWorkItemMockMvc.perform(get("/api/work-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())))
            .andExpect(jsonPath("$.[*].actualeffort").value(hasItem(DEFAULT_ACTUALEFFORT.toString())))
            .andExpect(jsonPath("$.[*].taskname").value(hasItem(DEFAULT_TASKNAME.toString())))
            .andExpect(jsonPath("$.[*].codingEffort").value(hasItem(DEFAULT_CODING_EFFORT.intValue())))
            .andExpect(jsonPath("$.[*].indates").value(hasItem(DEFAULT_INDATES)));
    }
    
    @Test
    @Transactional
    public void getWorkItem() throws Exception {
        // Initialize the database
        workItemRepository.saveAndFlush(workItem);

        // Get the workItem
        restWorkItemMockMvc.perform(get("/api/work-items/{id}", workItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(workItem.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()))
            .andExpect(jsonPath("$.actualeffort").value(DEFAULT_ACTUALEFFORT.toString()))
            .andExpect(jsonPath("$.taskname").value(DEFAULT_TASKNAME.toString()))
            .andExpect(jsonPath("$.codingEffort").value(DEFAULT_CODING_EFFORT.intValue()))
            .andExpect(jsonPath("$.indates").value(DEFAULT_INDATES));
    }

    @Test
    @Transactional
    public void getNonExistingWorkItem() throws Exception {
        // Get the workItem
        restWorkItemMockMvc.perform(get("/api/work-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWorkItem() throws Exception {
        // Initialize the database
        workItemRepository.saveAndFlush(workItem);

        int databaseSizeBeforeUpdate = workItemRepository.findAll().size();

        // Update the workItem
        WorkItem updatedWorkItem = workItemRepository.findById(workItem.getId()).get();
        // Disconnect from session so that the updates on updatedWorkItem are not directly saved in db
        em.detach(updatedWorkItem);
        updatedWorkItem
            .note(UPDATED_NOTE)
            .actualeffort(UPDATED_ACTUALEFFORT)
            .taskname(UPDATED_TASKNAME)
            .codingEffort(UPDATED_CODING_EFFORT)
            .indates(UPDATED_INDATES);

        restWorkItemMockMvc.perform(put("/api/work-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWorkItem)))
            .andExpect(status().isOk());

        // Validate the WorkItem in the database
        List<WorkItem> workItemList = workItemRepository.findAll();
        assertThat(workItemList).hasSize(databaseSizeBeforeUpdate);
        WorkItem testWorkItem = workItemList.get(workItemList.size() - 1);
        assertThat(testWorkItem.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testWorkItem.getActualeffort()).isEqualTo(UPDATED_ACTUALEFFORT);
        assertThat(testWorkItem.getTaskname()).isEqualTo(UPDATED_TASKNAME);
        assertThat(testWorkItem.getCodingEffort()).isEqualTo(UPDATED_CODING_EFFORT);
        assertThat(testWorkItem.getIndates()).isEqualTo(UPDATED_INDATES);
    }

    @Test
    @Transactional
    public void updateNonExistingWorkItem() throws Exception {
        int databaseSizeBeforeUpdate = workItemRepository.findAll().size();

        // Create the WorkItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkItemMockMvc.perform(put("/api/work-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(workItem)))
            .andExpect(status().isBadRequest());

        // Validate the WorkItem in the database
        List<WorkItem> workItemList = workItemRepository.findAll();
        assertThat(workItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteWorkItem() throws Exception {
        // Initialize the database
        workItemRepository.saveAndFlush(workItem);

        int databaseSizeBeforeDelete = workItemRepository.findAll().size();

        // Get the workItem
        restWorkItemMockMvc.perform(delete("/api/work-items/{id}", workItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WorkItem> workItemList = workItemRepository.findAll();
        assertThat(workItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkItem.class);
        WorkItem workItem1 = new WorkItem();
        workItem1.setId(1L);
        WorkItem workItem2 = new WorkItem();
        workItem2.setId(workItem1.getId());
        assertThat(workItem1).isEqualTo(workItem2);
        workItem2.setId(2L);
        assertThat(workItem1).isNotEqualTo(workItem2);
        workItem1.setId(null);
        assertThat(workItem1).isNotEqualTo(workItem2);
    }
}
