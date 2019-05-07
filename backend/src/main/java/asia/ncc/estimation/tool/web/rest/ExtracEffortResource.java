package asia.ncc.estimation.tool.web.rest;

import com.codahale.metrics.annotation.Timed;
import asia.ncc.estimation.tool.domain.ExtracEffort;
import asia.ncc.estimation.tool.repository.ExtracEffortRepository;
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
 * REST controller for managing ExtracEffort.
 */
@RestController
@RequestMapping("/api")
public class ExtracEffortResource {

    private final Logger log = LoggerFactory.getLogger(ExtracEffortResource.class);

    private static final String ENTITY_NAME = "extracEffort";

    private final ExtracEffortRepository extracEffortRepository;

    public ExtracEffortResource(ExtracEffortRepository extracEffortRepository) {
        this.extracEffortRepository = extracEffortRepository;
    }

    /**
     * POST  /extrac-efforts : Create a new extracEffort.
     *
     * @param extracEffort the extracEffort to create
     * @return the ResponseEntity with status 201 (Created) and with body the new extracEffort, or with status 400 (Bad Request) if the extracEffort has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/extrac-efforts")
    @Timed
    public ResponseEntity<ExtracEffort> createExtracEffort(@RequestBody ExtracEffort extracEffort) throws URISyntaxException {
        log.debug("REST request to save ExtracEffort : {}", extracEffort);
        if (extracEffort.getId() != null) {
            throw new BadRequestAlertException("A new extracEffort cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExtracEffort result = extracEffortRepository.save(extracEffort);
        return ResponseEntity.created(new URI("/api/extrac-efforts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /extrac-efforts : Updates an existing extracEffort.
     *
     * @param extracEffort the extracEffort to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated extracEffort,
     * or with status 400 (Bad Request) if the extracEffort is not valid,
     * or with status 500 (Internal Server Error) if the extracEffort couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/extrac-efforts")
    @Timed
    public ResponseEntity<ExtracEffort> updateExtracEffort(@RequestBody ExtracEffort extracEffort) throws URISyntaxException {
        log.debug("REST request to update ExtracEffort : {}", extracEffort);
        if (extracEffort.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExtracEffort result = extracEffortRepository.save(extracEffort);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, extracEffort.getId().toString()))
            .body(result);
    }

    /**
     * GET  /extrac-efforts : get all the extracEfforts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of extracEfforts in body
     */
    @GetMapping("/extrac-efforts")
    @Timed
    public List<ExtracEffort> getAllExtracEfforts() {
        log.debug("REST request to get all ExtracEfforts");
        return extracEffortRepository.findAll();
    }

    /**
     * GET  /extrac-efforts/:id : get the "id" extracEffort.
     *
     * @param id the id of the extracEffort to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the extracEffort, or with status 404 (Not Found)
     */
    @GetMapping("/extrac-efforts/{id}")
    @Timed
    public ResponseEntity<ExtracEffort> getExtracEffort(@PathVariable Long id) {
        log.debug("REST request to get ExtracEffort : {}", id);
        Optional<ExtracEffort> extracEffort = extracEffortRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(extracEffort);
    }

    /**
     * DELETE  /extrac-efforts/:id : delete the "id" extracEffort.
     *
     * @param id the id of the extracEffort to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/extrac-efforts/{id}")
    @Timed
    public ResponseEntity<Void> deleteExtracEffort(@PathVariable Long id) {
        log.debug("REST request to delete ExtracEffort : {}", id);

        extracEffortRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
