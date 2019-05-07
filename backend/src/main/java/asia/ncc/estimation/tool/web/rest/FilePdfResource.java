package asia.ncc.estimation.tool.web.rest;

import com.codahale.metrics.annotation.Timed;
import asia.ncc.estimation.tool.domain.FilePdf;
import asia.ncc.estimation.tool.repository.FilePdfRepository;
import asia.ncc.estimation.tool.web.rest.errors.BadRequestAlertException;
import asia.ncc.estimation.tool.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing FilePdf.
 */
@RestController
@RequestMapping("/api")
public class FilePdfResource {

    private final Logger log = LoggerFactory.getLogger(FilePdfResource.class);

    private static final String ENTITY_NAME = "filePdf";

    private final FilePdfRepository filePdfRepository;

    public FilePdfResource(FilePdfRepository filePdfRepository) {
        this.filePdfRepository = filePdfRepository;
    }

    /**
     * POST  /file-pdfs : Create a new filePdf.
     *
     * @param filePdf the filePdf to create
     * @return the ResponseEntity with status 201 (Created) and with body the new filePdf, or with status 400 (Bad Request) if the filePdf has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/file-pdfs")
    @Timed
    public ResponseEntity<FilePdf> createFilePdf(@RequestBody FilePdf filePdf) throws URISyntaxException {
        log.debug("REST request to save FilePdf : {}", filePdf);
        if (filePdf.getId() != null) {
            throw new BadRequestAlertException("A new filePdf cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilePdf result = filePdfRepository.save(filePdf);
        return ResponseEntity.created(new URI("/api/file-pdfs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /file-pdfs : Updates an existing filePdf.
     *
     * @param filePdf the filePdf to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated filePdf,
     * or with status 400 (Bad Request) if the filePdf is not valid,
     * or with status 500 (Internal Server Error) if the filePdf couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/file-pdfs")
    @Timed
    public ResponseEntity<FilePdf> updateFilePdf(@RequestBody FilePdf filePdf) throws URISyntaxException {
        log.debug("REST request to update FilePdf : {}", filePdf);
        if (filePdf.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        FilePdf result = filePdfRepository.save(filePdf);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, filePdf.getId().toString()))
            .body(result);
    }

    /**
     * GET  /file-pdfs : get all the filePdfs.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of filePdfs in body
     */
    @GetMapping("/file-pdfs")
    @Timed
    public List<FilePdf> getAllFilePdfs() {
        log.debug("REST request to get all FilePdfs");
        return filePdfRepository.findAll();
    }

    /**
     * GET  /file-pdfs/:id : get the "id" filePdf.
     *
     * @param id the id of the filePdf to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the filePdf, or with status 404 (Not Found)
     */
    @GetMapping("/file-pdfs/{id}")
    @Timed
    public ResponseEntity<FilePdf> getFilePdf(@PathVariable Long id) {
        log.debug("REST request to get FilePdf : {}", id);
        Optional<FilePdf> filePdf = filePdfRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(filePdf);
    }

    /**
     * DELETE  /file-pdfs/:id : delete the "id" filePdf.
     *
     * @param id the id of the filePdf to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/file-pdf/{id}")
    @Timed
    public ResponseEntity<Void> deleteFilePdf(@PathVariable Long id) {
        log.debug("REST request to delete FilePdf : {}", id);
        FilePdf filePdf = filePdfRepository.findById(id).get();
        String path = filePdf.getLocation()+filePdf.getName();
        path.replace("\\","/");
        try
        {
            Files.deleteIfExists(Paths.get(path));
            filePdfRepository.deleteById(id);
        }
        catch(NoSuchFileException e)
        {
            System.out.println("No such file/directory exists");
        }
        catch(DirectoryNotEmptyException e)
        {
            System.out.println("Directory is not empty.");
        }
        catch(IOException e)
        {
            System.out.println("Invalid permissions.");
        }
        System.out.println("Deletion successful.");
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    @DeleteMapping("/fileallpdf")
    public ResponseEntity<Void> deleteallFilePdf() {
        List<FilePdf> filePdfs = filePdfRepository.findAll();
        for(FilePdf filePdf:filePdfs) {
            log.debug("REST request to delete FilePdf : {}", filePdf.getId());
            String path = filePdf.getLocation() + filePdf.getName();
            path.replace("\\", "/");
            try {
                Files.deleteIfExists(Paths.get(path));
                filePdfRepository.deleteById(filePdf.getId());
            } catch (NoSuchFileException e) {
                System.out.println("No such file/directory exists");
            } catch (DirectoryNotEmptyException e) {
                System.out.println("Directory is not empty.");
            } catch (IOException e) {
                System.out.println("Invalid permissions.");
            }
            System.out.println("Deletion successful.");
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, filePdfs.toString())).build();
    }
}
