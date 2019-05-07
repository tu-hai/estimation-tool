package asia.ncc.estimation.tool.service.dto;

import asia.ncc.estimation.tool.domain.Assumption;
import asia.ncc.estimation.tool.domain.ExtracEffort;
import asia.ncc.estimation.tool.domain.WorkItem;

import java.util.List;

public class CreateQuotationDTO {
    private Long projectID;
    private Long customerID;
    private List<WorkItem> listworkItem;
    private List<ExtracEffort> effortList;
    private List<Assumption> assumptionList;

    public List<Assumption> getAssumptionList() {
        return assumptionList;
    }

    public void setAssumptionList(List<Assumption> assumptionList) {
        this.assumptionList = assumptionList;
    }

    public List<ExtracEffort> getEffortList() {
        return effortList;
}

    public CreateQuotationDTO setEffortList(List<ExtracEffort> effortList) {
        this.effortList = effortList;
        return this;
    }

    public List<WorkItem> getListworkItem() {
        return listworkItem;
    }

    public CreateQuotationDTO setListworkItem(List<WorkItem> listworkItem) {
        this.listworkItem = listworkItem;
        return this;
    }

    public Long getProjectID() {
        return projectID;
    }

    public CreateQuotationDTO setProjectID(Long projectID) {
        this.projectID = projectID;
        return this;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public CreateQuotationDTO setCustomerID(Long customerID) {
        this.customerID = customerID;
        return this;
    }
}
