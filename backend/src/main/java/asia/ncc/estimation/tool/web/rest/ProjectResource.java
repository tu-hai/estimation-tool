package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.domain.*;
import asia.ncc.estimation.tool.repository.*;
import asia.ncc.estimation.tool.service.dto.*;
import com.codahale.metrics.annotation.Timed;
import asia.ncc.estimation.tool.web.rest.errors.BadRequestAlertException;
import asia.ncc.estimation.tool.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    private static final String ENTITY_NAME = "project";

    private final ProjectRepository projectRepository;

    @Autowired
    private WorkItemRepository workItemRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private QuotationRepository quotationRepository;
    @Autowired
    private ExtracEffortRepository extracEffortRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private AssumptionRepository assumptionRepository;
    @Autowired
    private WorkItemResource workItemResource;

    public ProjectResource(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * POST  /projects : Create a new project.
     *
     * @param project the project to create
     * @return the ResponseEntity with status 201 (Created) and with body the new project, or with status 400 (Bad Request) if the project has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projects")
    @Timed
    public ResponseEntity<Project> createProject(@RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to save Project : {}", project);
        if (project.getId() != null) {
            throw new BadRequestAlertException("A new project cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Project result = projectRepository.save(project);
        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projects : Updates an existing project.
     *
     * @param project the project to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated project,
     * or with status 400 (Bad Request) if the project is not valid,
     * or with status 500 (Internal Server Error) if the project couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projects")
    @Timed
    public ResponseEntity<Project> updateProject(@RequestBody Project project) throws URISyntaxException {
        log.debug("REST request to update Project : {}", project);
        if (project.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Project result = projectRepository.save(project);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, project.getId().toString()))
            .body(result);
    }
    /**
     * GET  /projects : get all the projects.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of projects in body
     */
    @GetMapping("/projects")
    @Timed
    public List<Project> getAllProjects(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Projects");
        return projectRepository.findAllWithEagerRelationships();
    }
    @GetMapping("/projectpage")
    @Timed
    public Page<Project> getAllProjects(@RequestParam(required = false, defaultValue = "false") boolean eagerload,
                                        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                        @RequestParam(name = "size", required = false, defaultValue = "5") Integer size,
                                        @RequestParam(name = "sort", required = false, defaultValue = "ASC") String sort) {
        log.debug("REST request to get all Projects");
        Sort sortable = null;
        if (sort.equals("ASC")) {
            sortable = Sort.by("id").ascending();
        }
        if (sort.equals("DESC")) {
            sortable = Sort.by("id").descending();
        }
        Pageable pageable = PageRequest.of(page, size, sortable);
        return projectRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * GET  /projects/:id : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the project, or with status 404 (Not Found)
     */
    @GetMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        Optional<Project> project = projectRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(project);
    }

    /**
     * DELETE  /projects/:id : delete the "id" project.
     *
     * @param id the id of the project to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        List<WordItemDTO> wordItemDTOList = getworkitembyproject(id);
        for (WordItemDTO wordItemDTO:wordItemDTOList){
            workItemResource.deleteWorkItem(wordItemDTO.getNo());
        }
        List<Quotation> quotationList = quotationRepository.findAll();
        for (Quotation quotation:quotationList){
            System.out.println("ID project in quotation: "+quotation.getProjectID().getId());
            if(quotation.getProjectID().getId()==id) quotationRepository.delete(quotation);
        }
        projectRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    @GetMapping("/getAllDto")
    public List<ProjectDTO> getProjects(){
        List<Project> lstProject = projectRepository.findAll();
        List<ProjectDTO> listProjectDTO = new ArrayList<>();
        for (Project project: lstProject
             ) {
                ProjectDTO projectDTo = new ProjectDTO();
                projectDTo.setId(project.getId().toString());
                projectDTo.setName(project.getName());
                if(project.getProjecTypeId()!=null){projectDTo.setCategory(project.getProjecTypeId().getName());}
                projectDTo.setEndDate(project.getDateEnd());
                projectDTo.setStartDate(project.getDateStrart());
                if(project.getSizeId()!=null){projectDTo.setSize(project.getSizeId().getNameSize());}
                listProjectDTO.add(projectDTo);
        }
        return listProjectDTO;

    }
    @GetMapping("/WorkItemByProject/{id}")
    public List<WordItemDTO>  getworkitembyproject(@PathVariable Long id){

        List<WordItemDTO> st = new ArrayList<>();
        List<Project> list = projectRepository.findAll();
        for (Project p: list )
             {
                if(p.getId().equals(id)){
                Set<WorkItem> workItemIds = p.getWorkItemIds();
                long i=1;
                for (WorkItem workItem: workItemIds) {
                    WordItemDTO wordItemDTO = new WordItemDTO();
                    wordItemDTO.setNo(workItem.getId());
                    if(workItem.getCodingEffort()!=null)
                    wordItemDTO.setCodingErffort(workItem.getCodingEffort());
                    else wordItemDTO.setCodingErffort((long) 0);
                    wordItemDTO.setTaskName(workItem.getTaskname());
                    if(workItem.getIndates()!=null)
                    wordItemDTO.setIndayte(workItem.getIndates());
                    else wordItemDTO.setIndayte(0);
                    List<Assumption> assumptionList=assumptionRepository.findAll();
                    for (Assumption assumption:assumptionList){
                        if (assumption.getWorkItem().getId()==workItem.getId()){
                            wordItemDTO.setAssumptionConten(assumption.getContent());
                            wordItemDTO.setAssumptionNote(assumption.getNote());
                            break;
                        }
                    }
                    i++;
                    st.add(wordItemDTO);
                }
            }
        }
        return st;
    }

    @PostMapping("/CreateQuotation")
    @Transactional
    public void createQuotation(@RequestBody CreateQuotationDTO createQuotationDTO){
        Long projectId = createQuotationDTO.getProjectID();
        Long customerId = createQuotationDTO.getCustomerID();
        List<WorkItem> workItems = createQuotationDTO.getListworkItem();
        List<ExtracEffort> extracEfforts = createQuotationDTO.getEffortList();
        List<Assumption> assumptions=createQuotationDTO.getAssumptionList();

        if(projectRepository.findById(projectId).get()!=null) {
            Project project = projectRepository.findById(projectId).get();
            Customer customer = customerRepository.findById(customerId).get();
            int i=0;
            for (WorkItem workItem : workItems) {
                log.debug("workItem : {}", workItem);
                while (i<assumptions.size()) {
                    Assumption assumption= assumptions.get(i);
                    i++;
                    if (assumption.getContent()==null&&assumption.getNote()==null)break;
                    if (workItem.getId() == null) {
                        workItemRepository.save(workItem);
                        workItem.getProjectIds().add(project);
                        project.getWorkItemIds().add(workItem);
                    } else if (workItemRepository.existsById(workItem.getId())) {
                        Set<WorkItem> workItemList = project.getWorkItemIds();
                        int a = 0;
                        for (WorkItem workItem1 : workItemList) {
                            if (workItem1 == workItem) {
                                a++;
                                break;
                            }
                        }
                        if (a == 0) {
                            workItemRepository.save(workItem);
                            workItem.getProjectIds().add(project);
                            project.getWorkItemIds().add(workItem);
                        }
                    } else {
                        log.debug("Workitem unsave or not find in Database");
                    }
                    assumption.setWorkItem(workItem);
                    assumptionRepository.save(assumption);
                    break;
                }
            }

            for (ExtracEffort extracEffort : extracEfforts){
                if (extracEffort.getId()==null){
                  extracEffortRepository.save(extracEffort);
                  extracEffort.getProjectIds().add(project);
                  project.getEtracEffortIds().add(extracEffort);
                  projectRepository.save(project);
                }else if(extracEffortRepository.existsById(extracEffort.getId())){
                    Set<ExtracEffort> extracEffortset = project.getEtracEffortIds();
                    int a=0;
                    for(ExtracEffort extracEffort1:extracEffortset){
                        if (extracEffort1==extracEffort) {a++;break;}
                    }
                    if(a==0){
                        extracEffortRepository.save(extracEffort);
                        extracEffort.getProjectIds().add(project);
                        project.getEtracEffortIds().add(extracEffort);
                    }
                }else{
                    log.debug("ExtraEffort unsave or not find in Database");
                }
            }
            projectRepository.save(project);

            Quotation quotation = new Quotation();
            quotation.setCustomerId(customer);
            quotation.setProjectID(project);

            quotationRepository.save(quotation);
        }

    }


    @GetMapping("/projectsDTO/{id}")
    public ProjectDTO getProjectsDTO(@PathVariable Long id){
        List<Project> projects=projectRepository.findAll();
        ProjectDTO projectDTO=new ProjectDTO();
        for (Project project:projects){
            if (project.getId()==id){
                projectDTO.setId(String.valueOf(project.getId()));
                projectDTO.setName(project.getName());
                projectDTO.setStartDate(project.getDateStrart());
                projectDTO.setEndDate(project.getDateEnd());
                if (project.getSizeId()!=null)
                    projectDTO.setSize(project.getSizeId().getNameSize());
                if (project.getTechId()!=null) {
                    projectDTO.setTechName(project.getTechId().getName());
                    projectDTO.setTechContact(project.getTechId().getTechContact());
                    projectDTO.setTechDescripson(project.getTechId().getDescription());
                }
            }
        }
        return projectDTO;
    }

    @GetMapping("/getcount")
    public CountDTO getCount(){
        return testRepository.findcount();
    }
}
