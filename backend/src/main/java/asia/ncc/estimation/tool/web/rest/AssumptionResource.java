package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.domain.WorkItem;
import com.codahale.metrics.annotation.Timed;
import asia.ncc.estimation.tool.domain.Assumption;
import asia.ncc.estimation.tool.repository.AssumptionRepository;
import asia.ncc.estimation.tool.web.rest.errors.BadRequestAlertException;
import asia.ncc.estimation.tool.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Assumption.
 */
@RestController
@RequestMapping("/api")
public class AssumptionResource {

    private final Logger log = LoggerFactory.getLogger(AssumptionResource.class);

    private static final String ENTITY_NAME = "assumption";

    private final AssumptionRepository assumptionRepository;

    public AssumptionResource(AssumptionRepository assumptionRepository) {
        this.assumptionRepository = assumptionRepository;
    }

    /**
     * POST  /assumptions : Create a new assumption.
     *
     * @param assumption the assumption to create
     * @return the ResponseEntity with status 201 (Created) and with body the new assumption, or with status 400 (Bad Request) if the assumption has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/assumptions")
    @Timed
    public ResponseEntity<Assumption> createAssumption(@RequestBody Assumption assumption) throws URISyntaxException {
        log.debug("REST request to save Assumption : {}", assumption);
        if (assumption.getId() != null) {
            throw new BadRequestAlertException("A new assumption cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Assumption result = assumptionRepository.save(assumption);
        return ResponseEntity.created(new URI("/api/assumptions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /assumptions : Updates an existing assumption.
     *
     * @param assumption the assumption to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated assumption,
     * or with status 400 (Bad Request) if the assumption is not valid,
     * or with status 500 (Internal Server Error) if the assumption couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/assumptions")
    @Timed
    public ResponseEntity<Assumption> updateAssumption(@RequestBody Assumption assumption) throws URISyntaxException {
        log.debug("REST request to update Assumption : {}", assumption);
        if (assumption.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Assumption result = assumptionRepository.save(assumption);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, assumption.getId().toString()))
            .body(result);
    }

    /**
     * GET  /assumptions : get all the assumptions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of assumptions in body
     */
    @GetMapping("/assumptions")
    @Timed
    public List<Assumption> getAllAssumptions() {
        log.debug("REST request to get all Assumptions");
        return assumptionRepository.findAll();
    }

    /**
     * GET  /assumptions/:id : get the "id" assumption.
     *
     * @param id the id of the assumption to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the assumption, or with status 404 (Not Found)
     */
    @GetMapping("/assumptions/{id}")
    @Timed
    public ResponseEntity<Assumption> getAssumption(@PathVariable Long id) {
        log.debug("REST request to get Assumption : {}", id);
        Optional<Assumption> assumption = assumptionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(assumption);
    }

    /**
     * DELETE  /assumptions/:id : delete the "id" assumption.
     *
     * @param id the id of the assumption to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/assumptions/{id}")
    @Timed
    public ResponseEntity<Void> deleteAssumption(@PathVariable Long id) {
        log.debug("REST request to delete Assumption : {}", id);

        assumptionRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/assumptionbyidworkitem/{id}")
    public List<Assumption> getAssumptions(@PathVariable Long id){
        List<Assumption> listA = assumptionRepository.findAll();
        List<Assumption> listB = new ArrayList<>();
        for (Assumption a:listA){
            if(a.getWorkItem().getId()==id){
                listB.add(a);
            }
        }
        return listB;
    }
}
