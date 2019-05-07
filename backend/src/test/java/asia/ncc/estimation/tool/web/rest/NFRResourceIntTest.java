package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.EstimationToolApp;

import asia.ncc.estimation.tool.domain.NFR;
import asia.ncc.estimation.tool.repository.NFRRepository;
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
 * Test class for the NFRResource REST controller.
 *
 * @see NFRResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EstimationToolApp.class)
public class NFRResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private NFRRepository nFRRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restNFRMockMvc;

    private NFR nFR;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NFRResource nFRResource = new NFRResource(nFRRepository);
        this.restNFRMockMvc = MockMvcBuilders.standaloneSetup(nFRResource)
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
    public static NFR createEntity(EntityManager em) {
        NFR nFR = new NFR()
            .name(DEFAULT_NAME);
        return nFR;
    }

    @Before
    public void initTest() {
        nFR = createEntity(em);
    }

    @Test
    @Transactional
    public void createNFR() throws Exception {
        int databaseSizeBeforeCreate = nFRRepository.findAll().size();

        // Create the NFR
        restNFRMockMvc.perform(post("/api/nfrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nFR)))
            .andExpect(status().isCreated());

        // Validate the NFR in the database
        List<NFR> nFRList = nFRRepository.findAll();
        assertThat(nFRList).hasSize(databaseSizeBeforeCreate + 1);
        NFR testNFR = nFRList.get(nFRList.size() - 1);
        assertThat(testNFR.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createNFRWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = nFRRepository.findAll().size();

        // Create the NFR with an existing ID
        nFR.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNFRMockMvc.perform(post("/api/nfrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nFR)))
            .andExpect(status().isBadRequest());

        // Validate the NFR in the database
        List<NFR> nFRList = nFRRepository.findAll();
        assertThat(nFRList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllNFRS() throws Exception {
        // Initialize the database
        nFRRepository.saveAndFlush(nFR);

        // Get all the nFRList
        restNFRMockMvc.perform(get("/api/nfrs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nFR.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getNFR() throws Exception {
        // Initialize the database
        nFRRepository.saveAndFlush(nFR);

        // Get the nFR
        restNFRMockMvc.perform(get("/api/nfrs/{id}", nFR.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(nFR.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingNFR() throws Exception {
        // Get the nFR
        restNFRMockMvc.perform(get("/api/nfrs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNFR() throws Exception {
        // Initialize the database
        nFRRepository.saveAndFlush(nFR);

        int databaseSizeBeforeUpdate = nFRRepository.findAll().size();

        // Update the nFR
        NFR updatedNFR = nFRRepository.findById(nFR.getId()).get();
        // Disconnect from session so that the updates on updatedNFR are not directly saved in db
        em.detach(updatedNFR);
        updatedNFR
            .name(UPDATED_NAME);

        restNFRMockMvc.perform(put("/api/nfrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedNFR)))
            .andExpect(status().isOk());

        // Validate the NFR in the database
        List<NFR> nFRList = nFRRepository.findAll();
        assertThat(nFRList).hasSize(databaseSizeBeforeUpdate);
        NFR testNFR = nFRList.get(nFRList.size() - 1);
        assertThat(testNFR.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingNFR() throws Exception {
        int databaseSizeBeforeUpdate = nFRRepository.findAll().size();

        // Create the NFR

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNFRMockMvc.perform(put("/api/nfrs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(nFR)))
            .andExpect(status().isBadRequest());

        // Validate the NFR in the database
        List<NFR> nFRList = nFRRepository.findAll();
        assertThat(nFRList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNFR() throws Exception {
        // Initialize the database
        nFRRepository.saveAndFlush(nFR);

        int databaseSizeBeforeDelete = nFRRepository.findAll().size();

        // Get the nFR
        restNFRMockMvc.perform(delete("/api/nfrs/{id}", nFR.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<NFR> nFRList = nFRRepository.findAll();
        assertThat(nFRList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NFR.class);
        NFR nFR1 = new NFR();
        nFR1.setId(1L);
        NFR nFR2 = new NFR();
        nFR2.setId(nFR1.getId());
        assertThat(nFR1).isEqualTo(nFR2);
        nFR2.setId(2L);
        assertThat(nFR1).isNotEqualTo(nFR2);
        nFR1.setId(null);
        assertThat(nFR1).isNotEqualTo(nFR2);
    }
}
