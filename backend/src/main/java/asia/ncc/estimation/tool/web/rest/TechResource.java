package asia.ncc.estimation.tool.web.rest;

import com.codahale.metrics.annotation.Timed;
import asia.ncc.estimation.tool.domain.Tech;
import asia.ncc.estimation.tool.repository.TechRepository;
import asia.ncc.estimation.tool.web.rest.errors.BadRequestAlertException;
import asia.ncc.estimation.tool.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Tech.
 */
@RestController
@RequestMapping("/api")
public class TechResource {

    private final Logger log = LoggerFactory.getLogger(TechResource.class);

    private static final String ENTITY_NAME = "tech";

    private final TechRepository techRepository;

    public TechResource(TechRepository techRepository) {
        this.techRepository = techRepository;
    }

    /**
     * POST  /teches : Create a new tech.
     *
     * @param tech the tech to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tech, or with status 400 (Bad Request) if the tech has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/teches")
    @Timed
    public ResponseEntity<Tech> createTech(@RequestBody Tech tech) throws URISyntaxException {
        log.debug("REST request to save Tech : {}", tech);
        if (tech.getId() != null) {
            throw new BadRequestAlertException("A new tech cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Tech result = techRepository.save(tech);
        return ResponseEntity.created(new URI("/api/teches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /teches : Updates an existing tech.
     *
     * @param tech the tech to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tech,
     * or with status 400 (Bad Request) if the tech is not valid,
     * or with status 500 (Internal Server Error) if the tech couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/teches")
    @Timed
    public ResponseEntity<Tech> updateTech(@RequestBody Tech tech) throws URISyntaxException {
        log.debug("REST request to update Tech : {}", tech);
        if (tech.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Tech result = techRepository.save(tech);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, tech.getId().toString()))
            .body(result);
    }

    /**
     * GET  /teches : get all the teches.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of teches in body
     */
    @GetMapping("/teches")
    @Timed
    public List<Tech> getAllTeches() {
        log.debug("REST request to get all Teches");
        return techRepository.findAll();
    }

    /**
     * GET  /teches/:id : get the "id" tech.
     *
     * @param id the id of the tech to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tech, or with status 404 (Not Found)
     */
    @GetMapping("/teches/{id}")
    @Timed
    public ResponseEntity<Tech> getTech(@PathVariable Long id) {
        log.debug("REST request to get Tech : {}", id);
        Optional<Tech> tech = techRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tech);
    }

    /**
     * DELETE  /teches/:id : delete the "id" tech.
     *
     * @param id the id of the tech to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/teches/{id}")
    @Timed
    public ResponseEntity<Void> deleteTech(@PathVariable Long id) {
        log.debug("REST request to delete Tech : {}", id);
        techRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
