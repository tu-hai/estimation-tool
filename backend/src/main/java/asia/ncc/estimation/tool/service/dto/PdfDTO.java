package asia.ncc.estimation.tool.service.dto;

import asia.ncc.estimation.tool.domain.*;

import java.util.List;

public class PdfDTO {
    private List<NFR> nfrs;
    private List<Assumption> assumptions;
    private List<WorkItem> workItems;
    private List<ExtracEffort> extracEfforts;
    private List<Project> projects;
    private Project project;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<ExtracEffort> getExtracEfforts() {
        return extracEfforts;
    }

    public void setExtracEfforts(List<ExtracEffort> extracEfforts) {
        this.extracEfforts = extracEfforts;
    }

    public List<NFR> getNfrs() {
        return nfrs;
    }

    public PdfDTO setNfrs(List<NFR> nfrs) {
        this.nfrs = nfrs;
        return this;
    }

    public List<Assumption> getAssumptions() {
        return assumptions;
    }

    public PdfDTO setAssumptions(List<Assumption> assumptions) {
        this.assumptions = assumptions;
        return this;
    }

    public List<WorkItem> getWorkItems() {
        return workItems;
    }

    public PdfDTO setWorkItems(List<WorkItem> workItems) {
        this.workItems = workItems;
        return this;
    }
}
