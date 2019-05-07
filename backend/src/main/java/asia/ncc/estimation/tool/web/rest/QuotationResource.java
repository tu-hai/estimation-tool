package asia.ncc.estimation.tool.web.rest;

import asia.ncc.estimation.tool.domain.*;
import asia.ncc.estimation.tool.repository.*;
import asia.ncc.estimation.tool.service.MailService;
import asia.ncc.estimation.tool.service.dto.ListQDTO;
import asia.ncc.estimation.tool.service.dto.QuotationDTO;
import asia.ncc.estimation.tool.service.dto.WordItemDTO;
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
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.*;

/**
 * REST controller for managing Quotation.
 */
@RestController
@RequestMapping("/api")
public class QuotationResource {

    private final Logger log = LoggerFactory.getLogger(QuotationResource.class);

    private static final String ENTITY_NAME = "quotation";

    private final QuotationRepository quotationRepository;
    @Autowired
    TestRepository testRepository ;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private WorkItemRepository workItemRepository;
    @Autowired
    private AssumptionRepository assumptionRepository;


    public QuotationResource(QuotationRepository quotationRepository) {
        this.quotationRepository = quotationRepository;
    }

    /**
     * POST  /quotations : Create a new quotation.
     *
     * @param quotation the quotation to create
     * @return the ResponseEntity with status 201 (Created) and with body the new quotation, or with status 400 (Bad Request) if the quotation has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/quotations")
    @Timed
    public ResponseEntity<Quotation> createQuotation(@RequestBody Quotation quotation) throws URISyntaxException {
        log.debug("REST request to save Quotation : {}", quotation);
        if (quotation.getId() != null) {
            throw new BadRequestAlertException("A new quotation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Quotation result = quotationRepository.save(quotation);
        return ResponseEntity.created(new URI("/api/quotations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /quotations : Updates an existing quotation.
     *
     * @param quotation the quotation to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated quotation,
     * or with status 400 (Bad Request) if the quotation is not valid,
     * or with status 500 (Internal Server Error) if the quotation couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/quotations")
    @Timed
    public ResponseEntity<Quotation> updateQuotation(@RequestBody Quotation quotation) throws URISyntaxException {
        log.debug("REST request to update Quotation : {}", quotation);
        if (quotation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Quotation result = quotationRepository.save(quotation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, quotation.getId().toString()))
            .body(result);
    }

    /**
     * GET  /quotations : get all the quotations.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of quotations in body
     */
    @GetMapping("/quotations")
    @Timed
    public List<Quotation> getAllQuotations() {
        log.debug("REST request to get all Quotations");
        return quotationRepository.findAll();
    }

    /**
     * GET  /quotations/:id : get the "id" quotation.
     *
     * @param id the id of the quotation to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the quotation, or with status 404 (Not Found)
     */
    @GetMapping("/quotations/{id}")
    @Timed
    public ResponseEntity<Quotation> getQuotation(@PathVariable Long id) {
        log.debug("REST request to get Quotation : {}", id);
        Optional<Quotation> quotation = quotationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(quotation);
    }

    /**
     * DELETE  /quotations/:id : delete the "id" quotation.
     *
     * @param id the id of the quotation to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/quotations/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuotation(@PathVariable Long id) {
        log.debug("REST request to delete Quotation : {}", id);

        quotationRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    @GetMapping("/getQuotationByProjects/{id}")
    public ResponseEntity<Quotation> getQuotationByProjects(@PathVariable Long id) {
        List<Quotation> projectbyQuotaion = quotationRepository.findAll();
        for (Quotation quotation : projectbyQuotaion
        ) {
            if (quotation.getProjectID().getId() == id) {
                Optional<Quotation> st = Optional.of(quotation);
                return ResponseUtil.wrapOrNotFound(st);
            }
        }
        return null;
    }

    @GetMapping("/getQuotationByCustomer/{id}")
    public ResponseEntity<Quotation> getQuotationByCustomer(@PathVariable Long id) {
        List<Quotation> projectbyQuotaion = quotationRepository.findAll();
        for (Quotation quotation : projectbyQuotaion
        ) {
            if (quotation.getCustomerId().getId() == id) {
                Optional<Quotation> st = Optional.of(quotation);
                return ResponseUtil.wrapOrNotFound(st);
            }
        }
        return null;
    }

    @DeleteMapping("/delCustomer/{id}")
    public void deleteQuotationByCustomer(@PathVariable Long id) {
        log.debug("REST request to delete Quotation by Customer: {}", id);
        List<Quotation> list = quotationRepository.findAll();
        List<Customer> listC = customerRepository.findAll();
        Customer customer= new Customer();
        for (Quotation quotation : list) {
            if (quotation==null) break;
            if (quotation.getCustomerId().getId() == id) {
                quotationRepository.delete(quotation);
                customerRepository.delete(customer);
            }
        }
        for (Customer customer1: listC){
            if (customer1.getId()==id){
                customer=customer1;
                customerRepository.delete(customer);
            }
        }
    }

    @DeleteMapping("/delQuotationsById/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuotationbyId(@PathVariable Long id) {
        log.debug("REST request to delete Quotation : {}", id);
        quotationRepository.deleteById(id);
        testRepository.deleteCustomerByIdQuotation(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    @GetMapping("/getTotalbyQuotationId/{id}")
    public  double getcodingList(@PathVariable Long id) {
        List<Quotation> quotations = quotationRepository.findAll();
        double total= 0;
        double percentSum=0;
        List<Long> codingeff=new ArrayList<>();
        List<Long> percentEffort=new ArrayList<>();
        for (Quotation quotation : quotations){
            if (quotation.getId() == id){
                log.debug("workitems:  {}",quotation.getProjectID().getWorkItemIds());
                for(WorkItem workItem: quotation.getProjectID().getWorkItemIds()){
                   codingeff.add(workItem.getCodingEffort());
                }
                for (ExtracEffort extracEffort: quotation.getProjectID().getEtracEffortIds()){
                    percentEffort.add(extracEffort.getPercentEfforts());
                }
                log.debug("codingList:  {}",codingeff);
                break;
            }
        }

        for (int i=0;i<percentEffort.size();i++)
            if (percentEffort.get(i)!=null)
                percentSum=percentSum+percentEffort.get(i);
        for (int i=0;i<codingeff.size();i++)
            if (codingeff.get(i)!=null)
                total=total+codingeff.get(i);
        return total+(total*(percentSum/100));
    }
    @GetMapping("/quotationsDTO")
    public List<QuotationDTO> getQuotationDTOs(){
        List<Quotation> quotations=quotationRepository.findAll();
        List<QuotationDTO> quotationDTOList=new ArrayList<>();
        List<Assumption> assumptionList= assumptionRepository.findAll();
        Set<WorkItem> workItems=new HashSet<>();
        for (Quotation quotation:quotations){
            QuotationDTO quotationDTO=new QuotationDTO();
            List<Assumption> assumptions=new ArrayList<>();
            workItems=quotation.getProjectID().getWorkItemIds();
            for (WorkItem workItem:workItems) {
                for (Assumption assumption : assumptionList) {
                    if (workItem.getId()==assumption.getWorkItem().getId()){
                        assumptions.add(assumption);
                    }
                }
            }
            quotationDTO.setId(quotation.getId());
            quotationDTO.setCustomerId(quotation.getCustomerId());
            quotationDTO.setProjectId(quotation.getProjectID());
            quotationDTO.setAssumptionList(assumptions);
            quotationDTO.setTotal(getcodingList(quotation.getId()));
            quotationDTOList.add(quotationDTO);
        }
        return quotationDTOList;
    }
    @GetMapping("/quotationpage")
    @Timed
    public ListQDTO getAllquotations(@RequestParam(required = false, defaultValue = "false") boolean eagerload,
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
        Page<Quotation> quotationPage = quotationRepository.findAllWithEagerRelationships(pageable);
        List<Quotation> quotations=quotationPage.getContent();
        List<QuotationDTO> quotationDTOList=new ArrayList<>();
        ListQDTO listQDTO = new ListQDTO();
        List<Assumption> assumptionList= assumptionRepository.findAll();
        Set<WorkItem> workItems=new HashSet<>();
        for (Quotation quotation:quotations){
            QuotationDTO quotationDTO=new QuotationDTO();
            List<Assumption> assumptions=new ArrayList<>();
            workItems=quotation.getProjectID().getWorkItemIds();
            for (WorkItem workItem:workItems) {
                for (Assumption assumption : assumptionList) {
                    if (workItem.getId()==assumption.getWorkItem().getId()){
                        assumptions.add(assumption);
                    }
                }
            }
            quotationDTO.setId(quotation.getId());
            quotationDTO.setCustomerId(quotation.getCustomerId());
            quotationDTO.setProjectId(quotation.getProjectID());
            quotationDTO.setAssumptionList(assumptions);
            quotationDTO.setTotal(getcodingList(quotation.getId()));
            quotationDTOList.add(quotationDTO);
        }
        listQDTO.setContent(quotationDTOList);
        listQDTO.setTotalElements(quotationPage.getTotalElements());
        listQDTO.setTotalPages(quotationPage.getTotalPages());
        return  listQDTO;
    }
    @GetMapping("/quotationsDTO/{id}")
    public QuotationDTO getQuotationDTOs(@PathVariable Long id){
        List<Quotation> quotations=quotationRepository.findAll();
        List<Assumption> assumptionList= assumptionRepository.findAll();
        QuotationDTO quotationDTO = new QuotationDTO();
        Set<WorkItem> workItems=new HashSet<>();
        List<WordItemDTO> wordItemDTOS=new ArrayList<>();
        for (Quotation quotation:quotations){
            if (quotation.getId()==id) {
                List<Assumption> assumptions=new ArrayList<>();
                workItems=quotation.getProjectID().getWorkItemIds();
                for (WorkItem workItem:workItems) {
                    WordItemDTO wordItemDTO=new WordItemDTO();
                    if(workItem.getId()!=null)
                    wordItemDTO.setNo(workItem.getId());
                    List<Assumption> assumptionList1=testRepository.findAssByIdWork(workItem.getId());
                    System.out.println("List Assumption: "+assumptionList1);
                    if(workItem.getTaskname()!=null)
                    wordItemDTO.setTaskName(workItem.getTaskname());
                    if (workItem.getCodingEffort()!=null)
                    wordItemDTO.setCodingErffort(workItem.getCodingEffort());
                    if (workItem.getIndates()!=null)
                    wordItemDTO.setIndayte(workItem.getIndates());
                    for (Assumption assumption : assumptionList1) {
                        wordItemDTO.setAssumptionNote(assumption.getNote());
                        wordItemDTO.setAssumptionConten(assumption.getContent());
                    }
                    wordItemDTOS.add(wordItemDTO);
                }
                quotationDTO.setId(quotation.getId());
                quotationDTO.setCustomer(quotation.getCustomerId().getId());
                quotationDTO.setProject(quotation.getProjectID().getId());
                quotationDTO.setAssumptionList(assumptions);
                quotationDTO.setWordItemDTOList(wordItemDTOS);
                quotationDTO.setTotal(getcodingList(quotation.getId()));
            }
        }
        return quotationDTO;
    }

    @GetMapping("/quotationsDTO/{idp}/{idc}")
    public QuotationDTO getQuotationDTOs(@PathVariable Long idp,@PathVariable Long idc){
        List<Quotation> quotations=quotationRepository.findAll();
        List<Assumption> assumptionList= assumptionRepository.findAll();
        QuotationDTO quotationDTO = new QuotationDTO();
        Set<WorkItem> workItems=new HashSet<>();
        List<WordItemDTO> wordItemDTOS=new ArrayList<>();
        Long idQuotation = testRepository.findIdquotationByPidCid(idp,idc);
        if(idQuotation!=0) {
            Quotation quotation = quotationRepository.findById(idQuotation).get();

            List<Assumption> assumptions = new ArrayList<>();
            workItems = quotation.getProjectID().getWorkItemIds();
            for (WorkItem workItem : workItems) {
                WordItemDTO wordItemDTO = new WordItemDTO();
                if (workItem.getId() != null) wordItemDTO.setNo(workItem.getId());
                List<Assumption> assumptionList1 = testRepository.findAssByIdWork(workItem.getId());
                System.out.println("List Assumption: " + assumptionList1);
                if (workItem.getCodingEffort() != null)
                    wordItemDTO.setCodingErffort(workItem.getCodingEffort());
                if (workItem.getTaskname() != null)
                    wordItemDTO.setTaskName(workItem.getTaskname());
                if (workItem.getIndates() != null)
                    wordItemDTO.setIndayte(workItem.getIndates());
                for (Assumption assumption : assumptionList1) {
                    wordItemDTO.setAssumptionConten(assumption.getContent());
                    wordItemDTO.setAssumptionNote(assumption.getNote());
                }
                wordItemDTOS.add(wordItemDTO);
            }
            quotationDTO.setCustomer(quotation.getCustomerId().getId());
            quotationDTO.setId(quotation.getId());
            quotationDTO.setProject(quotation.getProjectID().getId());
            quotationDTO.setAssumptionList(assumptions);
            quotationDTO.setWordItemDTOList(wordItemDTOS);
            quotationDTO.setTotal(getcodingList(quotation.getId()));

            return quotationDTO;
        }
        else{
            System.out.println("Not find quotation in Data");
            return null;
        }
    }
    @PutMapping("/editquotation")
    @Timed
    public QuotationDTO editQuotation(@RequestBody QuotationDTO quotationDTO){

        log.debug("REST request to update Quotation : {}", quotationDTO);
        System.out.println(quotationDTO);
            List<Assumption> assumptionList= assumptionRepository.findAll();
            for(WordItemDTO wordItemDTO:quotationDTO.getWordItemDTOList()){
                for (Assumption assumption:assumptionList){
                    if(wordItemDTO.getNo()==assumption.getWorkItem().getId()){
                        WorkItem workItem=assumption.getWorkItem();
                        System.out.println("WorkItem in:"+workItem);
                        //if(wordItemDTO.getCodingErffort()!=null)
                        workItem.setCodingEffort(Long.valueOf(wordItemDTO.getCodingErffort()));
                        //if(wordItemDTO.getIndayte()!=null)
                        workItem.setIndates(Integer.valueOf(wordItemDTO.getIndayte()));
                        //if(wordItemDTO.getTaskName()!=null)
                        workItem.setTaskname(wordItemDTO.getTaskName());
                        System.out.println("WorkItem out:"+workItem);
                        workItemRepository.save(workItem);
                        assumption.setContent(wordItemDTO.getAssumptionConten());
                        assumption.setNote(wordItemDTO.getAssumptionNote());
                        assumptionRepository.save(assumption);
                        break;
                    }
                }
            }
        if (quotationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        return quotationDTO;
    }
}
