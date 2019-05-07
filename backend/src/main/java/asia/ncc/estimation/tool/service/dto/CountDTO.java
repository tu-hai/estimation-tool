package asia.ncc.estimation.tool.service.dto;

public class CountDTO {
    private int project;
    private int customer;
    private int quotation;
    private int workitem;

    public int getProject() {
        return project;
    }

    public CountDTO setProject(int project) {
        this.project = project;
        return this;
    }

    public int getCustomer() {
        return customer;
    }

    public CountDTO setCustomer(int customer) {
        this.customer = customer;
        return this;
    }

    public int getQuotation() {
        return quotation;
    }

    public CountDTO setQuotation(int quotation) {
        this.quotation = quotation;
        return this;
    }

    public int getWorkitem() {
        return workitem;
    }

    public CountDTO setWorkitem(int workitem) {
        this.workitem = workitem;
        return this;
    }
}
