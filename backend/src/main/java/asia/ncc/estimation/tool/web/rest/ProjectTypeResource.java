package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.domain.NFR;
import asia.ncc.estimation.tool.domain.Project;
import asia.ncc.estimation.tool.repository.NFRRepository;
import asia.ncc.estimation.tool.repository.ProjectRepository;
import asia.ncc.estimation.tool.service.dto.NfrDTO;
import asia.ncc.estimation.tool.service.dto.ProjectDTO;
import asia.ncc.estimation.tool.service.dto.ProjectTypeDTO;
import com.codahale.metrics.annotation.Timed;
import asia.ncc.estimation.tool.domain.ProjectType;
import asia.ncc.estimation.tool.repository.ProjectTypeRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ProjectType.
 */
@RestController
@RequestMapping("/api")
public class ProjectTypeResource {

    private final Logger log = LoggerFactory.getLogger(ProjectTypeResource.class);

    private static final String ENTITY_NAME = "projectType";

    private final ProjectTypeRepository projectTypeRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private NFRRepository nfrRepository;
    public ProjectTypeResource(ProjectTypeRepository projectTypeRepository) {
        this.projectTypeRepository = projectTypeRepository;
    }

    /**
     * POST  /project-types : Create a new projectType.
     *
     * @param projectType the projectType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new projectType, or with status 400 (Bad Request) if the projectType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/project-types")
    @Timed
    public ResponseEntity<ProjectType> createProjectType(@RequestBody ProjectType projectType) throws URISyntaxException {
        log.debug("REST request to save ProjectType : {}", projectType);
        if (projectType.getId() != null) {
            throw new BadRequestAlertException("A new projectType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProjectType result = projectTypeRepository.save(projectType);
        return ResponseEntity.created(new URI("/api/project-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /project-types : Updates an existing projectType.
     *
     * @param projectType the projectType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated projectType,
     * or with status 400 (Bad Request) if the projectType is not valid,
     * or with status 500 (Internal Server Error) if the projectType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/project-types")
    @Timed
    public ResponseEntity<ProjectType> updateProjectType(@RequestBody ProjectType projectType) throws URISyntaxException {
        log.debug("REST request to update ProjectType : {}", projectType);
        if (projectType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProjectType result = projectTypeRepository.save(projectType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, projectType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /project-types : get all the projectTypes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of projectTypes in body
     */
    @GetMapping("/project-types")
    @Timed
    public List<ProjectType> getAllProjectTypes() {
        log.debug("REST request to get all ProjectTypes");
        return projectTypeRepository.findAll();
    }

    /**
     * GET  /project-types/:id : get the "id" projectType.
     *
     * @param id the id of the projectType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the projectType, or with status 404 (Not Found)
     */
    @GetMapping("/project-types/{id}")
    @Timed
    public ResponseEntity<ProjectType> getProjectType(@PathVariable Long id) {
        log.debug("REST request to get ProjectType : {}", id);
        Optional<ProjectType> projectType = projectTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(projectType);
    }

    /**
     * DELETE  /project-types/:id : delete the "id" projectType.
     *
     * @param id the id of the projectType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/project-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteProjectType(@PathVariable Long id) {
        log.debug("REST request to delete ProjectType : {}", id);

        projectTypeRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    @GetMapping("/newProjectType")
    public List<ProjectTypeDTO>  getnewProjectType(){
        List<ProjectType> projectTypes=projectTypeRepository.findAll();
        List<Project> projects=projectRepository.findAll();
        List<NFR> nfrs=nfrRepository.findAll();
        List<ProjectTypeDTO> projectTypeDTOList=new ArrayList<>();

        for (ProjectType projectType: projectTypes){
            ProjectTypeDTO projectTypeDTO= new ProjectTypeDTO();
            projectTypeDTO.setId(projectType.getId());
            projectTypeDTO.setDecription(projectType.getDecription());
            projectTypeDTO.setTechnogory(projectType.getTechnogory());
            projectTypeDTO.setName(projectType.getName());
            List<ProjectDTO> strings=new ArrayList<>();
            for (Project project:projects) {
                ProjectDTO projectDTO=new ProjectDTO();
                if (project.getProjecTypeId().getId()==projectType.getId()) {
                    projectDTO.setId(String.valueOf(project.getId()));
                    projectDTO.setName(project.getName());
                    projectDTO.setStartDate(project.getDateStrart());
                    projectDTO.setEndDate(project.getDateEnd());
                    strings.add(projectDTO);
                }
                projectTypeDTO.setProjectDTO(strings);
            }
            List<NfrDTO> strings1=new ArrayList<>();
            for (NFR nfr: nfrs) {
                NfrDTO nfrDTO=new NfrDTO();
                if (projectType.getId()== nfr.getProjectType().getId()) {
                    nfrDTO.setId(String.valueOf(nfr.getId()));
                    nfrDTO.setName(nfr.getName());
                    strings1.add(nfrDTO);
                }
                projectTypeDTO.setNfrDTOS(strings1);
            }
            projectTypeDTOList.add(projectTypeDTO);
        }

        return projectTypeDTOList;
    }
}
