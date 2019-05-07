package asia.ncc.estimation.tool.web.rest;

import com.codahale.metrics.annotation.Timed;
import asia.ncc.estimation.tool.domain.Size;
import asia.ncc.estimation.tool.repository.SizeRepository;
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
 * REST controller for managing Size.
 */
@RestController
@RequestMapping("/api")
public class SizeResource {

    private final Logger log = LoggerFactory.getLogger(SizeResource.class);

    private static final String ENTITY_NAME = "size";

    private final SizeRepository sizeRepository;

    public SizeResource(SizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    /**
     * POST  /sizes : Create a new size.
     *
     * @param size the size to create
     * @return the ResponseEntity with status 201 (Created) and with body the new size, or with status 400 (Bad Request) if the size has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sizes")
    @Timed
    public ResponseEntity<Size> createSize(@RequestBody Size size) throws URISyntaxException {
        log.debug("REST request to save Size : {}", size);
        if (size.getId() != null) {
            throw new BadRequestAlertException("A new size cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Size result = sizeRepository.save(size);
        return ResponseEntity.created(new URI("/api/sizes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sizes : Updates an existing size.
     *
     * @param size the size to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated size,
     * or with status 400 (Bad Request) if the size is not valid,
     * or with status 500 (Internal Server Error) if the size couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sizes")
    @Timed
    public ResponseEntity<Size> updateSize(@RequestBody Size size) throws URISyntaxException {
        log.debug("REST request to update Size : {}", size);
        if (size.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Size result = sizeRepository.save(size);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, size.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sizes : get all the sizes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sizes in body
     */
    @GetMapping("/sizes")
    @Timed
    public List<Size> getAllSizes() {
        log.debug("REST request to get all Sizes");
        return sizeRepository.findAll();
    }

    /**
     * GET  /sizes/:id : get the "id" size.
     *
     * @param id the id of the size to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the size, or with status 404 (Not Found)
     */
    @GetMapping("/sizes/{id}")
    @Timed
    public ResponseEntity<Size> getSize(@PathVariable Long id) {
        log.debug("REST request to get Size : {}", id);
        Optional<Size> size = sizeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(size);
    }

    /**
     * DELETE  /sizes/:id : delete the "id" size.
     *
     * @param id the id of the size to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sizes/{id}")
    @Timed
    public ResponseEntity<Void> deleteSize(@PathVariable Long id) {
        log.debug("REST request to delete Size : {}", id);

        sizeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
