package asia.ncc.estimation.tool.service.dto;

import asia.ncc.estimation.tool.domain.Assumption;
import asia.ncc.estimation.tool.domain.Customer;
import asia.ncc.estimation.tool.domain.Project;

import java.util.List;

public class QuotationDTO {
    private Long id;
    private Project projectId;
    private Customer customerId;
    private Long project;
    private Long customer;
    private double total;
    private List<Assumption> assumptionList;
    private List<WordItemDTO> wordItemDTOList;


    public Long getProject() {
        return project;
    }

    public void setProject(Long project) {
        this.project = project;
    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public List<WordItemDTO> getWordItemDTOList() {
        return wordItemDTOList;
    }

    public void setWordItemDTOList(List<WordItemDTO> wordItemDTOList) {
        this.wordItemDTOList = wordItemDTOList;
    }

    public List<Assumption> getAssumptionList() {
        return assumptionList;
    }

    public void setAssumptionList(List<Assumption> assumptionList) {
        this.assumptionList = assumptionList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProjectId() {
        return projectId;
    }

    public void setProjectId(Project projectId) {
        this.projectId = projectId;
    }

    public Customer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Customer customerId) {
        this.customerId = customerId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
