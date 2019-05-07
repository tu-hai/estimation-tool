package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.repository.TestRepository;
import com.codahale.metrics.annotation.Timed;
import asia.ncc.estimation.tool.domain.WorkItem;
import asia.ncc.estimation.tool.repository.WorkItemRepository;
import asia.ncc.estimation.tool.web.rest.errors.BadRequestAlertException;
import asia.ncc.estimation.tool.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WorkItem.
 */
@RestController
@RequestMapping("/api")
public class WorkItemResource {

    private final Logger log = LoggerFactory.getLogger(WorkItemResource.class);

    private static final String ENTITY_NAME = "workItem";

    private final WorkItemRepository workItemRepository;
    @Autowired
    TestRepository testRepository ;

    public WorkItemResource(WorkItemRepository workItemRepository) {
        this.workItemRepository = workItemRepository;
    }

    /**
     * POST  /work-items : Create a new workItem.
     *
     * @param workItem the workItem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new workItem, or with status 400 (Bad Request) if the workItem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/work-items")
    @Timed
    public ResponseEntity<WorkItem> createWorkItem(@RequestBody WorkItem workItem) throws URISyntaxException {
        log.debug("REST request to save WorkItem : {}", workItem);
        if (workItem.getId() != null) {
            throw new BadRequestAlertException("A new workItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkItem result = workItemRepository.save(workItem);
        return ResponseEntity.created(new URI("/api/work-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /work-items : Updates an existing workItem.
     *
     * @param workItem the workItem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated workItem,
     * or with status 400 (Bad Request) if the workItem is not valid,
     * or with status 500 (Internal Server Error) if the workItem couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/work-items")
    @Timed
    public ResponseEntity<WorkItem> updateWorkItem(@RequestBody WorkItem workItem) throws URISyntaxException {
        log.debug("REST request to update WorkItem : {}", workItem);
        if (workItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WorkItem result = workItemRepository.save(workItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, workItem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /work-items : get all the workItems.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of workItems in body
     */
    @GetMapping("/work-items")
    @Timed
    public List<WorkItem> getAllWorkItems() {
        log.debug("REST request to get all WorkItems");
        return workItemRepository.findAll();
    }

    /**
     * GET  /work-items/:id : get the "id" workItem.
     *
     * @param id the id of the workItem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the workItem, or with status 404 (Not Found)
     */
    @GetMapping("/work-items/{id}")
    @Timed
    public ResponseEntity<WorkItem> getWorkItem(@PathVariable Long id) {
        log.debug("REST request to get WorkItem : {}", id);
        Optional<WorkItem> workItem = workItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workItem);
    }

    /**
     * DELETE  /work-items/:id : delete the "id" workItem.
     *
     * @param id the id of the workItem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/work-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteWorkItem(@PathVariable Long id) {
        log.debug("REST request to delete WorkItem : {}", id);

        testRepository.deleteAssumptiopnByIdWorkItem(id);
        testRepository.deleteWBSByIdWORK_ITEM(id);
        workItemRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    @DeleteMapping("/delWBS/{idw}/{idp}")
    public String delateWBS(@PathVariable Long idw,@PathVariable Long idp){
        System.out.println("Id project: "+idp);
        System.out.println("Id workitem: "+idw);
        log.debug("REST request to delete WorkItem by id Workitem: {} and id Project: {}", idw,idp);
        testRepository.deleteWBSByIdWORK_ITEMandIdPROJECT(idw,idp);
        return "Delete WBS success";
    }
}
