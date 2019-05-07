package asia.ncc.estimation.tool.web.rest;

import com.codahale.metrics.annotation.Timed;
import asia.ncc.estimation.tool.domain.NFR;
import asia.ncc.estimation.tool.repository.NFRRepository;
import asia.ncc.estimation.tool.web.rest.errors.BadRequestAlertException;
import asia.ncc.estimation.tool.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing NFR.
 */
@RestController
@RequestMapping("/api")
public class NFRResource {

    private final Logger log = LoggerFactory.getLogger(NFRResource.class);

    private static final String ENTITY_NAME = "nFR";

    private final NFRRepository nFRRepository;

    public NFRResource(NFRRepository nFRRepository) {
        this.nFRRepository = nFRRepository;
    }

    /**
     * POST  /nfrs : Create a new nFR.
     *
     * @param nFR the nFR to create
     * @return the ResponseEntity with status 201 (Created) and with body the new nFR, or with status 400 (Bad Request) if the nFR has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/nfrs")
    @Timed
    public ResponseEntity<NFR> createNFR(@RequestBody NFR nFR) throws URISyntaxException {
        log.debug("REST request to save NFR : {}", nFR);
        if (nFR.getId() != null) {
            throw new BadRequestAlertException("A new nFR cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NFR result = nFRRepository.save(nFR);
        return ResponseEntity.created(new URI("/api/nfrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /nfrs : Updates an existing nFR.
     *
     * @param nFR the nFR to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated nFR,
     * or with status 400 (Bad Request) if the nFR is not valid,
     * or with status 500 (Internal Server Error) if the nFR couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/nfrs")
    @Timed
    public ResponseEntity<NFR> updateNFR(@RequestBody NFR nFR) throws URISyntaxException {
        log.debug("REST request to update NFR : {}", nFR);
        if (nFR.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        NFR result = nFRRepository.save(nFR);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, nFR.getId().toString()))
            .body(result);
    }

    /**
     * GET  /nfrs : get all the nFRS.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of nFRS in body
     */
    @GetMapping("/nfrs")
    @Timed
    public List<NFR> getAllNFRS() {
        log.debug("REST request to get all NFRS");
        return nFRRepository.findAll();
    }

    /**
     * GET  /nfrs/:id : get the "id" nFR.
     *
     * @param id the id of the nFR to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the nFR, or with status 404 (Not Found)
     */
    @GetMapping("/nfrs/{id}")
    @Timed
    public ResponseEntity<NFR> getNFR(@PathVariable Long id) {
        log.debug("REST request to get NFR : {}", id);
        Optional<NFR> nFR = nFRRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(nFR);
    }

    /**
     * DELETE  /nfrs/:id : delete the "id" nFR.
     *
     * @param id the id of the nFR to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/nfrs/{id}")
    @Timed
    public ResponseEntity<Void> deleteNFR(@PathVariable Long id) {
        log.debug("REST request to delete NFR : {}", id);

        nFRRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/NFRbyidprojecttype/{id}")
    public List<NFR> getAssumptions(@PathVariable Long id) {
        List<NFR> listA = nFRRepository.findAll();
        List<NFR> listB = new ArrayList<>();
        for (NFR a : listA) {
            if (a.getProjectType().getId() == id) {
                listB.add(a);
            }
        }
        return listB;
    }
    @RequestMapping("/nfrss")
    public List<NFR> listNFR(
                               @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                               @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
                               @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("id").ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by("id").descending();
        }
        Pageable pageable = PageRequest.of(page, size, sortable);

        return nFRRepository.findNFRs(pageable).getContent();
    }
}
